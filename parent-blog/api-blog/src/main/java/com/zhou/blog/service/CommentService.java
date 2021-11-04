package com.zhou.blog.service;

import com.zhou.blog.vo.params.CommentParam;
import com.zhou.blog.vo.Result;



public interface CommentService {

    Result commentsByArticleId(Long id);

    Result comment(CommentParam commentParam);
}
