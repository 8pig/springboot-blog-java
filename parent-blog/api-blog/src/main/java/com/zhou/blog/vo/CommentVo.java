package com.zhou.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zhou.blog.dao.pojo.SysUser;
import lombok.Data;

/*
*  评论列表vo
*
*
* */


import java.util.List;

@Data
public class CommentVo  {
    //防止前端 精度损失 把id转为string
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private UserVo author;

    private String content;

    private List<CommentVo> childrens;

    private String createDate;

    private Integer level;

    private UserVo toUser;
}
