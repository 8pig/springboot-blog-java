package com.zhou.blog.vo;

import lombok.Data;

@Data
public class CommentParam {

    private Long id;

    private String content;

    private Long parent;

    private Long toUserId;


}
