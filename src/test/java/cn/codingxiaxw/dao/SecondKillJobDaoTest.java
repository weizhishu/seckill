package cn.codingxiaxw.dao;

import cn.codingxiaxw.entity.SecondKillJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by codingBoy on 16/11/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SecondKillJobDaoTest {

    @Resource
    private SecondKillJobDao secondKillJobDao;

    @Test
    public void insertSuccessKilled() throws Exception {

        long productId=1000L;
        long userPhone=13476191877L;
        int insertCount=secondKillJobDao.insertSecondKillJob(productId,userPhone);
        System.out.println("insertCount="+insertCount);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long productId=1000L;
        long userPhone=13476191877L;
        SecondKillJob secondKillJob=secondKillJobDao.queryByIdWithProduct(productId,userPhone);
        if(secondKillJob != null) {
        System.out.println(secondKillJob);
        System.out.println(secondKillJob.getProduct());
        }

    }

}