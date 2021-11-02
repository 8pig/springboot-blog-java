package com.zhou.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class TagVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private  String tagName;
}
