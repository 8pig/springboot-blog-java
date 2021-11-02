package com.zhou.blog.handler;


import com.alibaba.fastjson.JSON;
import com.zhou.blog.dao.pojo.SysUser;
import com.zhou.blog.service.LoginService;
import com.zhou.blog.vo.ErrorCode;
import com.zhou.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
        * 1.  判断请求的接口路径是否为 handlermethod
        * 2. 判断token 是否为空
        * 3. 如果token 不为空 登录验证 logginservice checktoken
        * 4. 如果认证成功 放行
        *
        * */


        if(!(handler instanceof HandlerMethod)){
            /* 可能是 资源 requestresourcehandler
            *   springboot 访问静态资源 默认去classpath static 访问
            *
            * */
            return  true;
        }

        String token = request.getHeader("Authorization");
        if(StringUtils.isBlank(token)) {
            Result res = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(res));
            return  false;
        }

        SysUser sysUser = loginService.check(token);
        if(sysUser == null){
            Result res = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(res));
            return  false;
        }



        // 验证完成 放行
        return true;
    }

}
