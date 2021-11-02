package com.zhou.blog.handler;

import com.zhou.blog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


/* aop 实现  加了controller 注解的 做拦截*/
@ControllerAdvice
public class AllExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result doException (Exception e) {
        e.printStackTrace();
        return Result.fail(-999, "系统异常");
    }
}
