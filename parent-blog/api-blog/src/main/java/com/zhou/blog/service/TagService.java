package com.zhou.blog.service;


import com.zhou.blog.vo.Result;
import com.zhou.blog.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> findTagsByArticleId(Long articleId);
    Result hots(int limit);

    /*
    * 查询所有文章标签
    * */
    Result findAll();
}
