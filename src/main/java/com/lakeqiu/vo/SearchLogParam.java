package com.lakeqiu.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 前端传过来的数据类。需要转化为符合数据库类型的Dto
 * @author lakeqiu
 */
@Getter
@Setter
@ToString
public class SearchLogParam {
    private Integer type;
    private String beforeSeg;
    private String afterSeg;
    private String operator;
    private String formTime;
    private String toTime;
}
