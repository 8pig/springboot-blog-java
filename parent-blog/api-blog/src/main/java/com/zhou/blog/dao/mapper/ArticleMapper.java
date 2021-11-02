package com.zhou.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhou.blog.dao.dos.Archives;
import com.zhou.blog.dao.pojo.Article;
import org.springframework.stereotype.Component;

import java.util.List;

/*
* 泛型的 article 要对于table Article
* */
@Component
public interface ArticleMapper extends BaseMapper<Article> {

    List<Archives> listArchives();
}
