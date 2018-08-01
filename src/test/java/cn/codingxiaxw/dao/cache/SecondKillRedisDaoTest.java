package cn.codingxiaxw.dao.cache;

import cn.codingxiaxw.dao.ProductDao;
import cn.codingxiaxw.entity.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by codingBoy on 17/2/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SecondKillRedisDaoTest {

    private long id = 1001;

    @Autowired
    private SecondKillRedisDao secondKillRedisDao;

    @Autowired
    private ProductDao productDao;

    @Test
    public void testSeckill() {
        //get and put

        Product product = secondKillRedisDao.getProduct(id);
        if (product == null) {
            product = productDao.queryById(id);
            if (product != null) {
                String result = secondKillRedisDao.putProduct(product);
                System.out.println(result);
                product = secondKillRedisDao.getProduct(id);
                System.out.println(product);
            }
        }
    }

    @Test
    public void getFromRedisOrDb() {
        Product seckill = secondKillRedisDao.getOrPutProduct(id, i -> productDao.queryById(i));
        Assert.assertEquals(1001, seckill.getProductId());
        Assert.assertNotNull(secondKillRedisDao.getProduct(id));
    }

}