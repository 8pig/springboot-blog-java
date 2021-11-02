package com.zhou.blog.controller;

import com.zhou.blog.service.LoginService;
import com.zhou.blog.vo.Result;
import com.zhou.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login (@RequestBody LoginParam loginParam) {


        return loginService.login(loginParam);
    }
}
