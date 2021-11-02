package com.zhou.blog.controller;

import com.zhou.blog.service.LoginService;
import com.zhou.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("logout")
public class LogoutController {

    @Autowired
    LoginService loginService;

    @GetMapping
    public Result logout(@RequestHeader("Authorization") String token){
        return loginService.logout(token);
    }
}
