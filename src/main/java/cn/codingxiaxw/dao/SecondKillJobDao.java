package cn.codingxiaxw.dao;

import cn.codingxiaxw.entity.SecondKillJob;
import org.apache.ibatis.annotations.Param;

/**
 * Created by codingBoy on 16/11/27.
 */
public interface SecondKillJobDao {

    /**
     * 插入购买明细,可过滤重复
     * @param productId
     * @param userPhone
     * @return插入的行数
     */
    int insertSecondKillJob(@Param("productId") long productId, @Param("userPhone") long userPhone);


    /**
     * 根据秒杀商品的id查询明细SuccessKilled对象(该对象携带了Seckill秒杀产品对象)
     * @param productId
     * @return
     */
    SecondKillJob queryByIdWithProduct(@Param("productId") long productId,@Param("userPhone") long userPhone);

}
