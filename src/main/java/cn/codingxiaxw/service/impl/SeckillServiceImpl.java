package cn.codingxiaxw.service.impl;

import cn.codingxiaxw.dao.ProductDao;
import cn.codingxiaxw.dao.SecondKillJobDao;
import cn.codingxiaxw.dao.cache.SecondKillRedisDao;
import cn.codingxiaxw.dto.Exposer;
import cn.codingxiaxw.dto.SeckillExecution;
import cn.codingxiaxw.entity.Product;
import cn.codingxiaxw.entity.SecondKillJob;
import cn.codingxiaxw.enums.SeckillStatEnum;
import cn.codingxiaxw.exception.RepeatKillException;
import cn.codingxiaxw.exception.SeckillCloseException;
import cn.codingxiaxw.exception.SeckillException;
import cn.codingxiaxw.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by codingBoy on 16/11/28.
 */
//@Component @Service @Dao @Controller
@Service
public class SeckillServiceImpl implements SeckillService {
    //日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //加入一个混淆字符串(秒杀接口)的salt，为了我避免用户猜出我们的md5值，值任意给，越复杂越好
    private final String salt = "shsdssljdd'l.";

    //注入Service依赖
    @Autowired //@Resource
    private ProductDao productDao;

    @Autowired //@Resource
    private SecondKillJobDao secondKillJobDao;

    @Autowired
    private SecondKillRedisDao secondKillRedisDao;

    @Override
    public List<Product> getProductList() {
        return productDao.queryAll(0, 4);
    }

    @Override
    public Product getById(long productId) {
        return secondKillRedisDao.getOrPutProduct(productId, id -> productDao.queryById(id));
    }

    @Override
    public Exposer exportProductUrl(long productId) {

        Product product = getById(productId);
        if(product != null) {
        //若是秒杀未开启
        Date startTime = product.getStartTime();
        Date endTime = product.getEndTime();
        //系统当前时间
        Date nowTime = new Date();
        if (startTime.getTime() > nowTime.getTime() || endTime.getTime() < nowTime.getTime()) {
            return new Exposer(false, productId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }}

        //秒杀开启，返回秒杀商品的id、用给接口加密的md5
        String md5 = getMD5(productId);
        return new Exposer(true, md5, productId);
    }

    private String getMD5(long productId) {
        String base = productId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    //秒杀是否成功，成功:减库存，增加明细；失败:抛出异常，事务回滚
    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点:
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作、只读操作不要事务控制
     */
    public SeckillExecution executeSeckill(long productId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {

        if (md5 == null || !md5.equals(getMD5(productId))) {
            //秒杀数据被重写了
            throw new SeckillException("product data rewrite");
        }
        //执行秒杀逻辑:减库存+增加购买明细
        Date nowTime = new Date();

        try {

            //否则更新了库存，秒杀成功,增加明细
            int insertCount = secondKillJobDao.insertSecondKillJob(productId, userPhone);
            //看是否该明细被重复插入，即用户是否重复秒杀
            if (insertCount <= 0) {
                throw new RepeatKillException("product repeated");
            } else {

                //减库存,热点商品竞争
                int updateCount = productDao.reduceNumber(productId, nowTime);
                if (updateCount <= 0) {
                    //没有更新库存记录，说明秒杀结束 rollback
                    throw new SeckillCloseException("product is closed");
                } else {
                    //秒杀成功,得到成功插入的明细记录,并返回成功秒杀的信息 commit
                    SecondKillJob secondKillJob = secondKillJobDao.queryByIdWithProduct(productId, userPhone);
                    return new SeckillExecution(productId, SeckillStatEnum.SUCCESS, secondKillJob);
                }

            }


        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所以编译期异常转化为运行期异常
            throw new SeckillException("product inner error :" + e.getMessage());
        }

    }
}







