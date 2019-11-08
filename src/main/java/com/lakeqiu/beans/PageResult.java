package com.lakeqiu.beans;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 存放分页结果
 * @author lakeqiu
 */
@Setter
@Getter
@ToString
@Builder
public class PageResult<T> {
    private List<T> data = Lists.newArrayList();

    private Integer total = 0;
}
