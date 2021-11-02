package com.zhou.blog.controller;


import com.zhou.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TsetController {
    @RequestMapping
    public Result test() {
        return Result.success(null);
    }
}
