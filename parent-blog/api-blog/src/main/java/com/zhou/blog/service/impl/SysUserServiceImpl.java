package com.zhou.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhou.blog.dao.mapper.SysUserMapper;
import com.zhou.blog.dao.pojo.SysUser;
import com.zhou.blog.service.LoginService;
import com.zhou.blog.service.SysUserService;
import com.zhou.blog.vo.ErrorCode;
import com.zhou.blog.vo.LoginUserVo;
import com.zhou.blog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    LoginService loginService;

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("匿名");
        }
        return sysUserMapper.selectById(id);
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.eq(SysUser::getPassword, password);
        queryWrapper.select(SysUser::getAccount, SysUser::getId, SysUser:: getAvatar, SysUser::getNickname);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    /*
    *
    * token 查询用户信息
    *  1. token 合法性校验
    *     是否为空  解析是否成功  redis 是否存在
    * 2. 如果失败 返回 错误
    * 3. 如果成功 返回对应的 结果
    *
    * */
    @Override
    public Result findUserByToken(String token) {
        SysUser sysUser = loginService.check(token);
        if(sysUser == null) {
            return Result.fail(ErrorCode.TOEKN_ERROR.getCode(), ErrorCode.TOEKN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setAvatar(sysUser.getAvatar());

        return  Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.last("limit 1");

        return this.sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        /*分布式id 雪花算法
        *
        * */
        this.sysUserMapper.insert(sysUser);
    }
}
