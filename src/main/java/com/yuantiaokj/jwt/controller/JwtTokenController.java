package com.yuantiaokj.jwt.controller;

import com.yuantiaokj.jwt.jwt.JwtSecurityProperties;
import com.yuantiaokj.jwt.jwt.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * ************************************************************
 * Copyright © 2020 远眺科技 Inc.All rights reserved.  *    **
 * ************************************************************
 *
 * @program: module-lib
 * @description: 测试
 * @author: cnzz
 * @create: 2020-06-04 16:39
 **/
@RestController
@Slf4j
@RequestMapping("/JwtTokenController")
@Api(tags = "jwt测试")
public class JwtTokenController {
    @Resource
    JwtTokenUtils jwtTokenUtils;

    /**
     * 登录认证生成token
     *
     * @return
     */
    @PostMapping("/getToken")
    @ApiOperation("生成token")
    public String getToken() {
        List<String> permissions = new ArrayList<>();
        permissions.add("admin");
        permissions.add("afds");
        String token = jwtTokenUtils.createToken("123", "cnzz", permissions);
        log.info("获取的|token={}", token);
        return token;
    }


    /**
     * 从token中获取用户信息
     *
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/getTokenUser")
    @ApiOperation("获取用户信息")
    public String getTokenUser(HttpServletRequest httpServletRequest) {
        //获取token
        String bearerToken = httpServletRequest.getHeader(JwtSecurityProperties.HEADER);
        log.info("获取token|bearerToken={}", bearerToken);
        //获取token用户信息
        Claims claims = jwtTokenUtils.getClaimsFromToken(bearerToken);
        String id = claims.getId();
        log.info("获取token用户信息|id={}", id);
        return id;
    }


    /**
     * 权限认证注解
     *
     * @return
     */
    @ApiOperation("权限认证注解")
    @PostMapping("/validPermission")
    @PreAuthorize("hasAnyAuthority('admin')")
    public String validPermission() {
        log.info("进入|validPermission");
        return "OK";
    }

}
