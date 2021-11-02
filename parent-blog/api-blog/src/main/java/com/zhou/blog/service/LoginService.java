package com.zhou.blog.service;

import com.zhou.blog.dao.pojo.SysUser;
import com.zhou.blog.vo.Result;
import com.zhou.blog.vo.params.LoginParam;
import org.springframework.transaction.annotation.Transactional;


public interface LoginService {
    Result login(LoginParam loginParam);

    SysUser check(String token);
    Result logout(String token);

    Result register(LoginParam loginParam);
}
