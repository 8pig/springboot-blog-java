package com.zhou.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhou.blog.dao.mapper.CategoryMapper;
import com.zhou.blog.dao.pojo.Category;
import com.zhou.blog.service.CategoryService;
import com.zhou.blog.vo.CategoryVo;
import com.zhou.blog.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {


    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    @Override
    public Result findAll() {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());

        return Result.success(copyList(categories));
    }

    private CategoryVo copy(Category categories) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(categories, categoryVo);
        return categoryVo;
    }
    private List<CategoryVo> copyList (List<Category> list) {
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : list) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }

}



