package com.yuantiaokj.jwt.globalException;

import com.yuantiaokj.commonmodule.base.SysRes;
import com.yuantiaokj.commonmodule.code.PubCode;
import com.yuantiaokj.commonmodule.exception.BizException;
import com.yuantiaokj.commonmodule.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * ************************************************************
 * Copyright © 2020 远眺科技 Inc.All rights reserved.  *    **
 * ************************************************************
 *
 * @program: redis-demo
 * @description:
 * @author: cnzz
 * @create: 2020-05-28 18:34
 **/

// 统一的异常类进行处理(把默认的异常返回信息改成自定义的异常返回信息)
// 当GlobalContrller抛出HospitalException异常时，将自动找到此类中对应的方法执行，并返回json数据给前台
@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)    //异常处理器，处理HospitalException异常
    public SysRes hanlerException(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        return SysRes.exception(e);
    }

    //异常处理器，处理HospitalException异常
    @ResponseBody
    @ExceptionHandler(value = ValidationException.class)
    public SysRes hanlerValidationException(HttpServletRequest request, ValidationException e) {
        e.printStackTrace();
        return SysRes.failure(e);
    }

    //异常处理器，处理HospitalException异常
    @ResponseBody
    @ExceptionHandler(value = BizException.class)
    public SysRes hanlerBizException(HttpServletRequest request, BizException e) {
        e.printStackTrace();
        return SysRes.failure(e);
    }

    //AuthenticationException
    @ResponseBody
    @ExceptionHandler(value = AuthenticationException.class)
    public SysRes hanlerAuthenticationException(HttpServletRequest request, AuthenticationException e) {
       // e.printStackTrace();
        log.debug("进入ExceptionController|PC_999001_重新登录");
        return SysRes.failure(new BizException(PubCode.PC_999001_重新登录));
    }

    //AccessDeniedException
    @ResponseBody
    @ExceptionHandler(value = AccessDeniedException.class)
    public SysRes hanlerAccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        //e.printStackTrace();
        log.debug("进入ExceptionController|PC_999401_无权限");
        return SysRes.failure(new BizException(PubCode.PC_999401_无权限));
    }
}

