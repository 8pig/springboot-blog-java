package com.zhou.blog.service;

import com.zhou.blog.vo.Result;
import com.zhou.blog.vo.params.ArticleParam;
import com.zhou.blog.vo.params.PageParams;

public interface ArticleService {

    /*
    * 分页查询 文章列表
    * */

    Result listArticle(PageParams pageParams);

    // 最热
    Result hotArticle(int limit);

    Result newArticle(int limit);

    // 文章归档 首页
    Result listArchives();


    // 查询文章详情
    Result findArticleById(Long articleId);

    Result publish(ArticleParam articleParam);
}
