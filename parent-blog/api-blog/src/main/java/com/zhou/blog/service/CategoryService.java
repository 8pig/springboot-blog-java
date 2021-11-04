package com.zhou.blog.service;

import com.zhou.blog.vo.CategoryVo;
import com.zhou.blog.vo.Result;

public interface CategoryService {

    CategoryVo findCategoryById(Long categoryId);

    Result findAll();
}
