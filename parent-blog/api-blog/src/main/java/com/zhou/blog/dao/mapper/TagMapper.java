package com.zhou.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhou.blog.dao.pojo.Tag;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TagMapper extends BaseMapper<Tag> {
    // 根据文章id 查询标签列表
    List<Tag> findTagsByArticleId(Long articleId);

    List<Long> findHotsTagIds(int limit);

    List<Tag> findTagsByTagIds(List<Long> tagIds);
}

