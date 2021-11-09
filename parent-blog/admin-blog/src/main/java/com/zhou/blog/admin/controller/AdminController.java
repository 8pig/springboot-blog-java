package com.zhou.blog.admin.controller;


import com.zhou.blog.admin.model.params.PageParam;
import com.zhou.blog.admin.pojo.Permission;
import com.zhou.blog.admin.service.impl.PermissionService;
import com.zhou.blog.admin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private PermissionService permissionService;



    @PostMapping("permission/permissionList")
    public Result listPermission (@RequestBody PageParam pageParam) {
        return  permissionService.listPermission(pageParam);
    }

    @PostMapping("permission/add")
    public Result add(@RequestBody Permission permission) {
        return permissionService.add(permission);
    }

    @PostMapping("permission/update")
    public Result update(@RequestBody Permission permission) {
        return permissionService.update(permission);
    }

    @GetMapping("permission/delete/{id}")
    public Result add(@PathVariable("id") Long id) {
        return permissionService.delete(id);
    }



}
