package cn.codingxiaxw.dao;

import cn.codingxiaxw.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;

/**
 * Created by codingBoy on 16/11/26.
 */
public interface ProductDao
{

    /**
     * 减库存
     * @param productId
     * @param killTime
     * @return 如果影响行数>1，表示更新库存的记录行数
     */
    int reduceNumber(@Param("productId") long productId, @Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀的商品信息
     * @param seckillId
     * @return
     */
    Product queryById(long productId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     */
    List<Product> queryAll(@Param("offset") int offset,@Param("limit") int limit);



}
