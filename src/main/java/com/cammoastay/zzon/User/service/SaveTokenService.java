package com.cammoastay.zzon.User.service;

import com.cammoastay.zzon.User.entity.UserRefresh;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SaveTokenService {
    @CachePut(cacheNames = "successfulAuthentication", key = "'userLoginId:' + #userLoginId", cacheManager = "refreshCacheManager")
    public UserRefresh cacheUserRefresh(String userLoginId, String refresh, Long expiredMs) {
        System.out.println("캐시 저장 메소드 호출됨------------------------------");

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        UserRefresh userRefresh = new UserRefresh();
        userRefresh.setUserLoginId(userLoginId);
        userRefresh.setRefresh(refresh);
        userRefresh.setExpiration(date.toString());

        return userRefresh;
    }
}
