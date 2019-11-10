package com.lakeqiu.service;

import com.lakeqiu.beans.CacheKeyConstants;

/**
 * @author lakeqiu
 */
public interface SysCacheService {
    /**
     * 保存缓存
     * @param toSavedValue 要保存的值
     * @param timeoutSeconds 超时时间
     * @param prefix 前缀
     */
    void saveCache(String toSavedValue, Integer timeoutSeconds, CacheKeyConstants prefix);

    /**
     * 获取缓存
     * @param prefix
     * @param keys
     * @return
     */
    String getFromCache(CacheKeyConstants prefix, String... keys);
}
