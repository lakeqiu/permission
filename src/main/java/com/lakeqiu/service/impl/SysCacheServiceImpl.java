package com.lakeqiu.service.impl;

import com.google.common.base.Joiner;
import com.lakeqiu.beans.CacheKeyConstants;
import com.lakeqiu.common.RedisPool;
import com.lakeqiu.service.SysCacheService;
import com.lakeqiu.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

/**
 * @author lakeqiu
 */
@Service
@Slf4j
public class SysCacheServiceImpl implements SysCacheService {

    @Autowired
    private RedisPool redisPool;

    /**
     * 保存缓存
     *
     * @param toSavedValue   要保存的值
     * @param timeoutSeconds 超时时间
     * @param prefix         前缀
     */
    @Override
    public void saveCache(String toSavedValue, Integer timeoutSeconds, CacheKeyConstants prefix) {
        saveCache(toSavedValue, timeoutSeconds, prefix, null);
    }

    /**
     * 使用这个是为了更好配合redis的api
     * @param toSavedValue
     * @param timeoutSeconds
     * @param prefix
     * @param keys
     */
    private void saveCache(String toSavedValue, Integer timeoutSeconds, CacheKeyConstants prefix, String... keys) {
        if (null == toSavedValue) {
            return;
        }

        // redis连接
        ShardedJedis shardedJedis = null;
        try {
            // 计算key
            String cacheKey = generateCacheKey(prefix, keys);
            // 获取连接
            shardedJedis = redisPool.instance();
            shardedJedis.setex(cacheKey, timeoutSeconds, toSavedValue);
        } catch (Exception e) {
            log.error("save cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapper.objToJson(keys), e);
        } finally {
            // 关闭连接
            redisPool.close(shardedJedis);
        }

    }

    /**
     * 获取缓存
     *
     * @param prefix
     * @param keys
     * @return
     */
    @Override
    public String getFromCache(CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String cacheKey = generateCacheKey(prefix, keys);
        try {
            shardedJedis = redisPool.instance();
            String value = shardedJedis.get(cacheKey);
            return value;
        } catch (Exception e) {
            log.error("get from cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapper.objToJson(keys), e);
            return null;
        } finally {
            redisPool.close(shardedJedis);
        }

    }

    /**
     * 计算key
     * @param prefix 前缀
     * @param keys key
     * @return
     */
    private String generateCacheKey(CacheKeyConstants prefix, String... keys) {
        String key = prefix.name();
        if (keys != null && keys.length > 0) {
            key += "_" + Joiner.on("_").join(keys);
        }
        return key;
    }
}
