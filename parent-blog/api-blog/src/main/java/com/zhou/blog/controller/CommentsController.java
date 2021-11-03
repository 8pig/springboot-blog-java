package com.zhou.blog.controller;


import com.zhou.blog.service.CommentService;
import com.zhou.blog.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("comments")
public class CommentsController {

    @Resource
    private CommentService commentService;

    /*
    *
    *
    * */
    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id) {



        return commentService.commentsByArticleId(id);
    }

}
