package com.cammoastay.zzon.user.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    //properties에 저장한 key를 기반으로 객체 키(JWT키)를 생성함.
    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret){

        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    //category 검증
    public String getCategory(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public Long getUserId(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", Long.class);
    }

    //userLoginId 검증
    public String getUserLoginId(String token) {

        //내가 생성한 secretkey가 맞는지 verify
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userLoginId", String.class);
    }

    //role 검증
    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    //token 만료 확인
    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    //issuedAt : 토큰이 언제 발행됐는지
    //category : Access / Refresh
    public String createJwt(String category, Long userId, String userLoginId, String role, Long expiredMs){
        return Jwts.builder()
                .claim("category", category)
                .claim("userId", userId)
                .claim("userLoginId", userLoginId)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
