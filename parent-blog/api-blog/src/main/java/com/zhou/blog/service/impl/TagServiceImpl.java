package com.zhou.blog.service.impl;

import com.zhou.blog.dao.mapper.TagMapper;
import com.zhou.blog.dao.pojo.Tag;
import com.zhou.blog.service.TagService;
import com.zhou.blog.vo.Result;
import com.zhou.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        // mybatis plus 无法多表查询
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);

        return copyList(tags);
    }



    public TagVo copy (Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
//        tagVo.setId(Long.valueOf(String.valueOf(tag.getId())));
        return tagVo;
    }
    public List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

/*
*
* */
    @Override
    public Result hots(int limit) {
        /*
        * 标签拥有的文章最多 就是最热标签
        * 根据tag_id 分组计数
        * 从大到小排列 取前limt个
        *
        * */

        List<Long> tagIds = tagMapper.findHotsTagIds(limit);
        if (CollectionUtils.isEmpty(tagIds)){
            return Result.success(Collections.emptyList());
        }
        List<Tag> tagList = tagMapper.findTagsByTagIds(tagIds);
        /* select * form tag where id in #{tags} s*/
        return Result.success(tagList);
    }
}
