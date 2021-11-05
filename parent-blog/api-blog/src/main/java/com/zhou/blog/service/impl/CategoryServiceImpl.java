package com.zhou.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId,Category::getCategoryName);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        return Result.success(copyList(categories));
    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        List<Category> categories = categoryMapper.selectList(queryWrapper);

        return Result.success(copyList(categories));
    }


    @Override
    public Result categoryDetailById(Long id) {
        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper();

        Category category = categoryMapper.selectById(id);
        return Result.success(copy(category));
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



