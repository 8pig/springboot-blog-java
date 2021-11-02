package com.zhou.blog.service;

import com.zhou.blog.dao.pojo.SysUser;
import com.zhou.blog.vo.Result;

public interface SysUserService {
    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);
    Result findUserByToken(String token);

    // 注册时查找用户是否存在
    SysUser findUserByAccount(String account);

    // 保存用户
    void save(SysUser sysUser);
}
