package com.zhou.blog.controller;

import com.zhou.blog.service.SysUserService;
import com.zhou.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    SysUserService sysUserService;

    @GetMapping("currentUser")
    public Result currentUser (@RequestHeader("Authorization") String token) {

        // 根据token 查询用户信息
        return sysUserService.findUserByToken(token);

    }
}
