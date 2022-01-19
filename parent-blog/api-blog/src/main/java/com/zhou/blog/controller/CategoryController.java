package com.zhou.blog.controller;


import com.zhou.blog.service.CategoryService;
import com.zhou.blog.vo.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("categorys")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping
    public Result categorysList() {
        return categoryService.findAll();
    }

    @GetMapping("detail")
    public Result findAllDetail (){
        return  categoryService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result categoryDetailById (@PathVariable("id") Long id){
        return  categoryService.categoryDetailById(id);
    }


}
