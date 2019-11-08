package com.lakeqiu.beans;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * 存放分页请求
 * @author lakeqiu
 */
public class PageQuery {
    @Getter
    @Setter
    @Min(value = 1, message = "当前页数不合法")
    private Integer pageNo = 1;

    @Getter
    @Setter
    @Min(value = 1, message = "每页展示数量不合法")
    private Integer pageSize = 10;

    @Setter
    private Integer offset;

    public Integer getOffset() {
        return (pageNo - 1) * pageSize;
    }
}
