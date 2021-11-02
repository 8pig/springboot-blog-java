package com.zhou.blog.controller;


import com.zhou.blog.service.LoginService;
import com.zhou.blog.vo.Result;
import com.zhou.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private LoginService loginService;


    @PostMapping
    public Result register(@RequestBody LoginParam loginParam) {
        return  loginService.register(loginParam);
    }

}
