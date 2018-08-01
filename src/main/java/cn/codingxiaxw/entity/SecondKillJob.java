package cn.codingxiaxw.entity;

import java.util.Date;

/**
 * Created by codingBoy on 16/11/24.
 */
public class SecondKillJob
{
    private long productId;
    private long userPhone;
    private short state;
    private Date createTime;

    //多对一,因为一件商品在库存中有很多数量，对应的购买明细也有很多。
    private Product product;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "SecondKillJob{" +
                "productId=" + productId +
                ", userPhone=" + userPhone +
                ", state=" + state +
                ", createTime=" + createTime +
                '}';
    }
}
