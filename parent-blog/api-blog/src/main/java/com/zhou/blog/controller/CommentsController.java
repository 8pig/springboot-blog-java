package com.zhou.blog.controller;


import com.zhou.blog.service.CommentService;
import com.zhou.blog.vo.params.CommentParam;
import com.zhou.blog.vo.Result;
import org.springframework.web.bind.annotation.*;

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
    public Result comments(@PathVariable("id") Long id){
        return commentService.commentsByArticleId(id);
    }

    /*
    *  评论接口
    * */
    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentService.comment(commentParam);
    }

}
