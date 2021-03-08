package com.yuantiaokj.jwt.jwt;

import com.alibaba.fastjson.JSON;
import com.yuantiaokj.commonmodule.base.SysRes;
import com.yuantiaokj.commonmodule.code.PubCode;
import com.yuantiaokj.commonmodule.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ************************************************************
 * Copyright © 2020 远眺科技 Inc.All rights reserved.  *    **
 * ************************************************************
 *
 * @program: redis-demo
 * @description: 无权限访问类
 * @author: cnzz
 * @create: 2020-05-26 08:58
 **/

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.debug("JwtAccessDeniedHandler|PC_999401_无权限");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(SysRes.failure(new BizException(PubCode.PC_999401_无权限))));
    }
}

