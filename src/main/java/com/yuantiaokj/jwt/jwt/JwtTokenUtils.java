package com.yuantiaokj.jwt.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.ByteBuffer;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ************************************************************
 * Copyright © 2020 远眺科技 Inc.All rights reserved.  *    **
 * ************************************************************
 *
 * @program: redis-demo
 * @description: jwt Token工具类
 * @author: cnzz
 * @create: 2020-05-26 09:01
 * <p>
 * -  jwtSecurityProperties 注入
 * -  afterPropertiesSet    jjwt secret HMAC key 生成
 * -  createToken   jwt 生成
 * -  getExpirationDateFromToken  jwt 中获取expiration期限时间
 * -  getAuthentication  获取用户认证SpringSecuity
 * -  validateToken jwt 校验
 * -  getClaimsFromToken jwt获取claims  jwtMap
 **/

@Slf4j
@Component
public class JwtTokenUtils implements InitializingBean {


    private final JwtSecurityProperties jwtSecurityProperties;
    private static final String AUTHORITIES_KEY = "auth";
    private Key key;

    public JwtTokenUtils(JwtSecurityProperties jwtSecurityProperties) {
        this.jwtSecurityProperties = jwtSecurityProperties;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecurityProperties.getBase64Secret());
        //MAC-SHA algorithms MUST have a size >= 256 bits
        ByteBuffer byteBuffer = ByteBuffer.allocate(256);
        byteBuffer.put(keyBytes);
        log.debug("keyBytes length={},byteBuffer length={}", keyBytes.length,byteBuffer.array().length);
        this.key = Keys.hmacShaKeyFor(byteBuffer.array());
    }


    /**
     * JWT token生成
     * <p>
     * iss: jwt签发者
     * sub: jwt所面向的用户
     * aud: 接收jwt的一方
     * exp: jwt的过期时间，这个过期时间必须要大于签发时间
     * nbf: 定义在什么时间之前，该jwt都是不可用的.
     * iat: jwt的签发时间
     * jti: jwt的唯一身份标识，主要用来作为一次性token。
     *
     * @param userId         用户标识
     * @param userName       用户
     * @param permissionList 权限list
     * @return String jwt
     */
    public String createToken(String userId, String userName, List<String> permissionList) {

        String newStr = permissionList.stream().collect(Collectors.joining(","));

        return Jwts.builder()
                .claim(AUTHORITIES_KEY, newStr)
                .setId(userId)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtSecurityProperties.getTokenValidityInSeconds()))
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * jwt 中获取expiration期限时间
     *
     * @param token jwt
     * @return Date
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * 获取用户认证SpringSecuity
     *
     * @param token jwt
     * @return Authentication
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        log.debug("我的filter|claims={}", claims);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        log.debug("我的filter|authorities={}", authorities);
        //HashMap map =(HashMap) claims.get("auth");

        //User principal = new User(map.get("user").toString(),map.get("password").toString(), authorities);
        String principal = (String) claims.get("sub");
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * jwt校验
     *
     * @param authToken jwt
     * @return boolean
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            e.printStackTrace();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * jwt 获取 jwtMap
     *
     * @param token jwt获取 Claims
     * @return Claims
     */
    public Claims getClaimsFromToken(String token) {

        //判断为空
        if (StringUtils.isEmpty(token)) {
            return null;
        }
//        if (StringUtils.hasText(token) && token.startsWith(JwtSecurityProperties.TOKEN_START_WITH)) {
//            token = token.substring(JwtSecurityProperties.TOKEN_START_WITH.length());
//        }

        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
            claims = null;
        }
        return claims;
    }
}

