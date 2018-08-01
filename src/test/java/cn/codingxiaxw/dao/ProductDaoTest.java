package cn.codingxiaxw.dao;

import cn.codingxiaxw.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by codingBoy on 16/11/27.
 * 配置spring和junit整合，这样junit在启动时就会加载spring容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class ProductDaoTest {

    //注入Dao实现类依赖
    @Resource
    private ProductDao productDao;


    @Test
    public void queryById() throws Exception {
        long productId=1000;
        Product product=productDao.queryById(productId);
        if(product != null) {
        System.out.println(product.getName());
        System.out.println(product);
        }
    }

    @Test
    public void queryAll() throws Exception {

        List<Product> products=productDao.queryAll(0,100);
        for (Product product : products)
        {
            System.out.println(product);
        }
    }

    @Test
    public void reduceNumber() throws Exception {

        long productId=1000;
        Date date=new Date();
        int updateCount=productDao.reduceNumber(productId,date);
        System.out.println(updateCount);

    }


}