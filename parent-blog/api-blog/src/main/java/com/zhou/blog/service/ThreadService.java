package com.zhou.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhou.blog.dao.mapper.ArticleMapper;
import com.zhou.blog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {



    @Async("taskExecutor")
    public void updateArticleViewCout(ArticleMapper articleMapper, Article article) {
        // 期望在线程池执行不会影响主线程
         int viewCounts = article.getViewCounts();
         Article articleUpdate = new Article();
         articleUpdate.setViewCounts(viewCounts +1 );
        LambdaQueryWrapper<Article> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(Article::getId, article.getId());
        updateWrapper.eq(Article::getViewCounts, viewCounts);
         articleMapper.update(articleUpdate, updateWrapper);

        try {
                Thread.sleep(5000);
                System.out.println("更新完成");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


    }
}
