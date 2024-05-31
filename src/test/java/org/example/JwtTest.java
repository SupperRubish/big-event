package org.example;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JwtTest {
    @Test
    public void testGen(){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",1);
        claims.put("name","张三");

        //生成Jwt
        String Token = JWT.create()
                .withClaim("user",claims)//添加载荷
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*12))//过期时间 12小时
                .sign(Algorithm.HMAC256("cheng"));//指定算法，配置密钥
        System.out.println(Token);
    }
    @Test
    public void testParse(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTY2MTYzODMsInVzZXIiOnsibmFtZSI6IuW8oOS4iSIsImlkIjoxfX0.5Zu0GY4PIvjKBdLfBU2qPsCEiepLrbssu2UYpzmJZ-8";
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("cheng")).build();//验证器
        DecodedJWT decodedJWT = jwtVerifier.verify(token);//解析后的JWT对象
        Map<String, Claim> claims = decodedJWT.getClaims();
        System.out.println(claims.get("user"));

    }
}
