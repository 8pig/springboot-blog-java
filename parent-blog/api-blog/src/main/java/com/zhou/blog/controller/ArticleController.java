package com.zhou.blog.controller;


import com.zhou.blog.common.aop.LogAnnotation;
import com.zhou.blog.common.cache.Cache;
import com.zhou.blog.service.ArticleService;
import com.zhou.blog.vo.Result;
import com.zhou.blog.vo.params.ArticleParam;
import com.zhou.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/*
*  json 数据交互 resetcontroller
* */
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    /*
    *  首页文章列表
    * @pageparams
    * */
    @PostMapping
    /* 加上注解 表示 对接口纪录日志  */
    @LogAnnotation(module="文章", operator="获取文章列表")
    @Cache(expire= 5 * 60 * 1000, name = "list_article")
    public Result articlesList (@RequestBody PageParams pageParams) {
        return articleService.listArticle(pageParams);
    }
    // 最热文章
    @PostMapping("hot")
    @Cache(expire= 5 * 60 * 1000, name = "hot_article")
    public Result hot () {
        int limit = 5;
        return articleService.hotArticle(limit);
    }
     // 最新文章
    @PostMapping("new")
    @Cache(expire= 5 * 60 * 1000, name = "news_article")
    public Result newArticle () {
        int limit = 5;
        return articleService.newArticle(limit);
    }

    // 首页文章归档
    @PostMapping("listArchives")
    public Result listArchives () {
        return articleService.listArchives();
    }

    //
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId) {
        return articleService.findArticleById(articleId);
    }


    /*
    *  新增文章
    *
    * */
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam) {
        return articleService.publish(articleParam);
    }
}
