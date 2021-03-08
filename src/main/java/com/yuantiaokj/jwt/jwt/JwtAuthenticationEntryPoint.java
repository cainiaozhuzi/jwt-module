package com.yuantiaokj.jwt.jwt;


import com.alibaba.fastjson.JSON;
import com.yuantiaokj.commonmodule.base.SysRes;
import com.yuantiaokj.commonmodule.code.PubCode;
import com.yuantiaokj.commonmodule.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
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
 * @description: 认证失败
 * @author: cnzz
 * @create: 2020-05-26 09:00
 **/

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.debug("进入JwtAuthenticationEntryPoint|PC_999001_重新登录");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(SysRes.failure(new BizException(PubCode.PC_999001_重新登录))));

        //throw new BizException(PubCode.PC_999001_重新登录);
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException==null?"Unauthorized":authException.getMessage());
    }
}
