package com.zhou.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.mysql.cj.log.Log;
import com.zhou.blog.dao.dos.Archives;
import com.zhou.blog.dao.mapper.ArticleBodyMapper;
import com.zhou.blog.dao.mapper.ArticleMapper;
import com.zhou.blog.dao.mapper.ArticleTagMapper;
import com.zhou.blog.dao.pojo.*;
import com.zhou.blog.service.*;
import com.zhou.blog.utils.UserThreadLocal;
import com.zhou.blog.vo.ArticleBodyVo;
import com.zhou.blog.vo.ArticleVo;
import com.zhou.blog.vo.Result;
import com.zhou.blog.vo.TagVo;
import com.zhou.blog.vo.params.ArticleParam;
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

    @Autowired
    private ArticleTagMapper articleTagMapper;


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
            articleVoList.add(copy(record, isTag, isAuthor, false, false));
        }
        return articleVoList;
    }

    /*
    *  重载
    * */
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor,boolean isBody, boolean isCategory ) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record: records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }

    @Autowired
    private CategoryService categoryService;
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor,boolean isBody, boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(Long.valueOf(String.valueOf(article.getId())));
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
        if(isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }

        if (isCategory) {
            try {
                Long categoryId = article.getCategoryId();
                articleVo.setCategory(categoryService.findCategoryById(categoryId));
            }catch (Exception e) {
                System.out.println(e);
            }
        }

        return articleVo;
    }

    @Resource
    private ArticleBodyMapper articleBodyMapper;
    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());


        return articleBodyVo;
    }


    @Autowired
    private ThreadService threadService;
    /*
    *  根据id查文章信息
    *   根据bodyid 和categoryid 做关联查询
    *
    * */
    @Override
    public Result findArticleById(Long articleId) {


        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true, true, true);
        threadService.updateArticleViewCout(articleMapper, article);
        return Result.success(articleVo);
    }


    /*
    *  save
    *   1. 构建article 对象
    * */
    @Override
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();
        /*
        *
        * 1. 作者id 当前登录的ID  想获得当前登录用户 得把需要获取的路由加到congig 中
        * 2. 标签 加入关联表中  tag
        * 3. body 内容存储
        *
        *
        * */
        // 插入后会生成 文章id
        Article article = new Article();
        article.setAuthorId(sysUser.getId());

        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(articleParam.getCategory().getId());


        articleMapper.insert(article);
        // tag 存储tag 表
        List<TagVo> tags = articleParam.getTags();
        if(tags != null) {
            Long articleId = article.getId();
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(tag.getId());
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        // body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());

        articleMapper.updateById(article);



        return null;
    }


}
