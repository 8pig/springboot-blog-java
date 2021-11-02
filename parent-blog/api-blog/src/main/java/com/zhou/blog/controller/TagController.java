package com.zhou.blog.controller;


import com.zhou.blog.service.TagService;
import com.zhou.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagController {
    @Autowired
    private TagService tagService;


    @GetMapping("hot")
    public Result hoot () {
        int limit = 6;
        return tagService.hots(limit);
    }

}
