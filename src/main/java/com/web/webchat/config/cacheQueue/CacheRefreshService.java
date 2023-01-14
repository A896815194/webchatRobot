package com.web.webchat.config.cacheQueue;


public interface CacheRefreshService {

    /**
     * 添加消息
     *
     * @param message 消息
     */
    boolean addMessage(final TSysCacheEvent message);
}
