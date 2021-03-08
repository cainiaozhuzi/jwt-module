package com.yuantiaokj.jwt.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * ************************************************************
 * Copyright © 2020 远眺科技 Inc.All rights reserved.  *    **
 * ************************************************************
 *
 * @program: redis-demo
 * @description: jwt配置类
 * @author: cnzz
 * @create: 2020-05-26 08:55
 **/

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtSecurityProperties {

    public static final String HEADER="token";
    //public static final String TOKEN_START_WITH="bearer";

    /**
     * Base64对该令牌进行编码
     */
    private String base64Secret;

    /**
     * 令牌过期时间 此处单位/毫秒
     */
    private Long tokenValidityInSeconds;

    private List<String> permitMatchers;

}

