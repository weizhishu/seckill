package cn.codingxiaxw.dao.cache;

import cn.codingxiaxw.entity.Product;
import cn.codingxiaxw.utils.JedisUtils;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;
import java.util.function.Function;

/**
 * Created by codingBoy on 17/2/17.
 */
public class SecondKillRedisDao {
    private final JedisPool jedisPool;

    public SecondKillRedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    private RuntimeSchema<Product> schema = RuntimeSchema.createFrom(Product.class);

    public Product getProduct(long productId) {
        return getProduct(productId, null);
    }

    /**
     * 从redis获取信息
     *
     * @param productId id
     * @return 如果不存在，则返回null
     */
    public Product getProduct(long productId, Jedis jedis) {
        boolean hasJedis = jedis != null;
        //redis操作逻辑
        try {
            if (!hasJedis) {
                jedis = jedisPool.getResource();
            }
            try {
                String key = getProductRedisKey(productId);
                //并没有实现哪部序列化操作
                //采用自定义序列化
                //protostuff: pojo.
                byte[] bytes = jedis.get(key.getBytes());
                //缓存重获取到
                if (bytes != null) {
                    Product product = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes, product, schema);
                    //product被反序列化

                    return product;
                }
            } finally {
                if (!hasJedis) {
                    jedis.close();
                }
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 从缓存获取，如果没有，则从数据库获取
     * 会用到分布式锁
     *
     * @param productId     id
     * @param getDataFromDb 从数据库获取的方法
     * @return 返回商品信息
     */
    public Product getOrPutProduct(long productId, Function<Long, Product> getDataFromDb) {

        String lockKey = "product:locks:getProduct:" + productId;
        String lockRequestId = UUID.randomUUID().toString();
        Jedis jedis = jedisPool.getResource();

        try {
            // 循环直到获取到数据
            while (true) {
                Product product = getProduct(productId, jedis);
                if (product != null) {
                    return product;
                }
                // 尝试获取锁。
                // 锁过期时间是防止程序突然崩溃来不及解锁，而造成其他线程不能获取锁的问题。过期时间是业务容忍最长时间。
                boolean getLock = JedisUtils.tryGetDistributedLock(jedis, lockKey, lockRequestId, 1000);
                if (getLock) {
                    // 获取到锁，从数据库拿数据, 然后存redis
                    product = getDataFromDb.apply(productId);
                    putProduct(product, jedis);
                    return product;
                }

                // 获取不到锁，睡一下，等会再出发。sleep的时间需要斟酌，主要看业务处理速度
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }
        } catch (Exception ignored) {
        } finally {
            // 无论如何，最后要去解锁
            JedisUtils.releaseDistributedLock(jedis, lockKey, lockRequestId);
            jedis.close();
        }
        return null;
    }

    /**
     * 根据id获取redis的key
     *
     * @param productId 商品id
     * @return redis的key
     */
    private String getProductRedisKey(long productId) {
        return "product:" + productId;
    }

    public String putProduct(Product product) {
        return putProduct(product, null);
    }

    public String putProduct(Product product, Jedis jedis) {
        boolean hasJedis = jedis != null;
        try {
            if (!hasJedis) {
                jedis = jedisPool.getResource();
            }
            try {
                String key = getProductRedisKey(product.getProductId());
                byte[] bytes = ProtostuffIOUtil.toByteArray(product, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存，1小时
                int timeout = 60 * 60;
                String result = jedis.setex(key.getBytes(), timeout, bytes);

                return result;
            } finally {
                if (!hasJedis) {
                    jedis.close();
                }
            }
        } catch (Exception e) {

        }

        return null;
    }
}
