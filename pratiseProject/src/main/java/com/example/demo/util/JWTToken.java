package com.example.demo.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * 生成jwttoken方法
 */
@Data
@Accessors(chain = true)
@Component
public class JWTToken implements AuthenticationToken{

    private static final long serialVersionUID = -8451637096112402805L;

    private String username;

    //private String password;(AuthenticationToken 不应该带入密码明文信息)

    private String clientId;

    private String jwtToken;

    @Override
    public Object getPrincipal() {
        return jwtToken;
    }

    @Override
    public Object getCredentials() {
        return jwtToken;
    }

    public JWTToken JWTToken(String username,String password,String clientId){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        String encodeds1 = DatatypeConverter.printBase64Binary("pwy".getBytes());
        byte[] keyBytes = DatatypeConverter.parseBase64Binary(encodeds1);//密钥key常量,可以存储在redis或数据库里
        Key signKey = new SecretKeySpec(keyBytes, signatureAlgorithm.getJcaName());
        int expiredTime = 1800;//过期时间，可以传人也可以后台自定义
        //开始创建jwt

        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("username", username)
                .claim("password", password)
                .claim("clientId", clientId)
                .claim("nowMillis",nowMillis)//时间标识区分token
                .signWith(signatureAlgorithm, signKey);
        if (expiredTime > 0) {
            Date expTime = new Date(nowMillis + expiredTime * 1000);
            builder.setExpiration(expTime).setNotBefore(now);
        }
        String jwtToken = builder.compact();
        return new JWTToken().setClientId(clientId)
                .setUsername(username)
                .setJwtToken(jwtToken);
    }

}
