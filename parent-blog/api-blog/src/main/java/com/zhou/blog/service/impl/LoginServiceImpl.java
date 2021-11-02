package com.zhou.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhou.blog.dao.pojo.SysUser;
import com.zhou.blog.service.LoginService;
import com.zhou.blog.service.SysUserService;
import com.zhou.blog.utils.JWTUtils;
import com.zhou.blog.vo.ErrorCode;
import com.zhou.blog.vo.Result;
import com.zhou.blog.vo.params.LoginParam;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String slat = "mszlu!@#";

    @Override
    public Result login(LoginParam loginParam) {
        /*
        *  1. 检查参数是否合法
        *  2. 根据用户名密码 去user 表查询 是否存在
        *  3. 如果不存在 登录失败
        *  4. 如果存在
        *  5. 使用jwt 生成token 给前端
        *  6. token 放入 redis 中   token： user  信息  ， 设置过期时间
        *  认证时  先认证token 是否合法  再去 redis 认证是否存在
        * */

        String password = loginParam.getPassword();
        String account = loginParam.getAccount();
        password = DigestUtils.md5Hex(password + slat);

        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return  Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser user = sysUserService.findUser(account, password);
        if(user == null) {
            return  Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JWTUtils.createToken(user.getId());

        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(user), 1, TimeUnit.DAYS);


        return Result.success(token);
    }

    @Override
    public SysUser check(String token) {
        if(StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if(stringObjectMap == null){
            return  null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if(StringUtils.isBlank(userJson)){
            return  null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);

        return sysUser;
    }

    // 退出
    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }


    // 注册
    @Override
    public Result register(LoginParam loginParam) {
        /*
        * 1. 是否合法
        * 2. 是否存在 存在返回已注册
        * 3. 不存在 注册用户
        * 4. 生成token
        * 5。 存redis 返回
        * 6. 加上事务  一旦中间任何过程出现问题  注册用户回滚
        * */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        if(
            StringUtils.isBlank(account)
            || StringUtils.isBlank(password)
            ||StringUtils.isBlank(nickname)
        ){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser user = sysUserService.findUserByAccount(loginParam.getAccount());
        if(user != null) {
            return Result.fail( ErrorCode.ACCOUNT_EXIST.getCode(),  ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        SysUser sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("");
        sysUser.setAdmin(1);
        sysUser.setDeleted(0);
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        this.sysUserService.save(sysUser);

        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);

        return Result.success(token);
    }

}
