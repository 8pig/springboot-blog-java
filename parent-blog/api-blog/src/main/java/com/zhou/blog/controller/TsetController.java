package com.zhou.blog.controller;


import com.zhou.blog.dao.pojo.SysUser;
import com.zhou.blog.utils.UserThreadLocal;
import com.zhou.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TsetController {
    @RequestMapping
    public Result test() {
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
