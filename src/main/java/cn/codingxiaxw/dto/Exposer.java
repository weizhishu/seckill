package cn.codingxiaxw.dto;

/**
 * Created by codingBoy on 16/11/27.
 * 暴露秒杀地址(接口)DTO
 */
public class Exposer {

    //是否开启秒杀
    private boolean exposed;

    //加密措施
    private String md5;

    private long productId;

    //系统当前时间(毫秒)
    private long now;

    //秒杀的开启时间
    private long start;

    //秒杀的结束时间
    private long end;

    public Exposer(boolean exposed, String md5, long productId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.productId = productId;
    }

    public Exposer(boolean exposed, long seckillId,long now, long start, long end) {
        this.exposed = exposed;
        this.productId=seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.productId = seckillId;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return productId;
    }

    public void setSeckillId(long seckillId) {
        this.productId = seckillId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "exposed=" + exposed +
                ", md5='" + md5 + '\'' +
                ", productId=" + productId +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
