package com.cammoastay.zzon.User.service;

import com.cammoastay.zzon.User.entity.UserRefresh;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final CacheManager cacheManager;

    public TokenService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Cacheable(cacheNames = "memberRefresh", key = "'refresh:' + #refresh ", cacheManager = "refreshCacheManager")
    public UserRefresh cacheUserRefresh(String userLoginId, String refresh, Long expiredMs) {

        UserRefresh userRefresh = new UserRefresh();
        userRefresh.setRefresh(refresh);

        return userRefresh;
    }

    public UserRefresh getUserRefresh(String refresh) {
        String key = "refresh:" + refresh;
        Cache cache = cacheManager.getCache("memberRefresh");
        if (cache != null) {
            return cache.get(key, UserRefresh.class);
        }
        return null;
    }

    // 캐시에서 특정 refresh 토큰을 삭제하는 메서드
    public void evictUserRefresh(String refresh) {
        String key = "refresh:" + refresh;
        Cache cache = cacheManager.getCache("memberRefresh");
        if (cache != null) {
            cache.evict(key); // 캐시에서 삭제
        }
    }
}
