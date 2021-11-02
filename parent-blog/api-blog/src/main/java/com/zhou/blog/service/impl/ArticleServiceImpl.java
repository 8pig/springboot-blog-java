package com.zhou.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.mysql.cj.log.Log;
import com.zhou.blog.dao.dos.Archives;
import com.zhou.blog.dao.mapper.ArticleMapper;
import com.zhou.blog.dao.pojo.Article;
import com.zhou.blog.service.ArticleService;
import com.zhou.blog.service.SysUserService;
import com.zhou.blog.service.TagService;
import com.zhou.blog.vo.ArticleVo;
import com.zhou.blog.vo.Result;
import com.zhou.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service

public class ArticleServiceImpl implements ArticleService {

    /*
    *  数据层注入
    * */
    @Autowired
    private  ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;


    @Override
    public Result listArticle(PageParams pageParams) {
        /*
        * 分页查询数据库
        * */
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<Article>();
        // 是否置顶排序
//        queryWrapper.orderByDesc(Article::getWeight);
        // order by create_data desc
        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);

        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);

        List<Article> records = articlePage.getRecords();

        List<ArticleVo> articleVoList = copyList(records, true, true);

        return  Result.success(articleVoList);
    }

    // 最热文章
    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<Article>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit "+ limit);
//        select id, title from article order by view_count desc limit 5
        List<Article> articleList = articleMapper.selectList(queryWrapper);

        return  Result.success(copyList(articleList, false, false));

    }


    // 最新文章
    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<Article>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit "+ limit);
//        select id, title from article order by create_time desc limit 5
        List<Article> articleList = articleMapper.selectList(queryWrapper);

        return  Result.success(copyList(articleList, false, false));

    }

    // 文章归档
    @Override
    public Result listArchives() {

        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor ) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record: records) {
            articleVoList.add(copy(record, isTag, isAuthor));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor){
        ArticleVo articleVo = new ArticleVo();
//        articleVo.setId(Long.valueOf(String.valueOf(article.getId())));
        BeanUtils.copyProperties(article,articleVo);

        System.out.println(article.toString());
        System.out.println(articleVo.toString());

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的接口 都需要标签 ，作者信息
        if (isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }

        return articleVo;
    }




}
