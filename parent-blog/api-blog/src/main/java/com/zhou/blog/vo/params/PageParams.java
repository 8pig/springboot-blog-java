package com.zhou.blog.vo.params;

import lombok.Data;

@Data
public class PageParams {

    private int page = 1;

    private int pageSize = 10;
}
