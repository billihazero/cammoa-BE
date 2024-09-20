package com.cammoastay.zzon.User.service;

import com.cammoastay.zzon.User.entity.UserRefresh;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SaveTokenService {

    private final CacheManager cacheManager;

    public SaveTokenService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Cacheable(cacheNames = "memberRefresh", key = "'userLoginId:' + #userLoginId + ':refresh:' + #refresh ", cacheManager = "refreshCacheManager")
    public UserRefresh cacheUserRefresh(String userLoginId, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        UserRefresh userRefresh = new UserRefresh();
        userRefresh.setUserLoginId(userLoginId);
        userRefresh.setRefresh(refresh);
        userRefresh.setExpiration(date.toString());

        return userRefresh;
    }

    public UserRefresh getUserRefresh(String userLoginId, String refresh) {
        String key = "userLoginId:" + userLoginId + ":refresh:" + refresh;
        Cache cache = cacheManager.getCache("memberRefresh");
        if (cache != null) {
            return cache.get(key, UserRefresh.class);
        }
        return null;
    }
}
