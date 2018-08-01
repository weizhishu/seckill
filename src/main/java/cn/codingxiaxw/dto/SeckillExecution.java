package cn.codingxiaxw.dto;

import cn.codingxiaxw.entity.SecondKillJob;
import cn.codingxiaxw.enums.SeckillStatEnum;

/**
 * 封装执行秒杀后的结果:是否秒杀成功
 * Created by codingBoy on 16/11/27.
 */
public class SeckillExecution {

    private long productId;

    //秒杀执行结果的状态
    private int state;

    //状态的明文标识
    private String stateInfo;

    //当秒杀成功时，需要传递秒杀成功的对象回去
    private SecondKillJob secondKillJob;

    //秒杀成功返回所有信息
    public SeckillExecution(long productId, SeckillStatEnum statEnum, SecondKillJob secondKillJob) {
        this.productId = productId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getInfo();
        this.secondKillJob = secondKillJob;
    }

    //秒杀失败
    public SeckillExecution(long productId, SeckillStatEnum statEnum) {
        this.productId = productId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getInfo();
    }

    public long getSeckillId() {
        return productId;
    }

    public void setSeckillId(long productId) {
        this.productId = productId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SecondKillJob getSuccessKilled() {
        return secondKillJob;
    }

    public void setSuccessKilled(SecondKillJob secondKillJob) {

        this.secondKillJob = secondKillJob;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "productId=" + productId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", secondKillJob=" + secondKillJob +
                '}';
    }
}
