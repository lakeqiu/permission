package com.lakeqiu.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author lakeqiu
 */
@Getter
@Setter
@ToString
public class SearchLogDto {
    private Integer type;
    private String beforeSeg;
    private String afterSeg;
    private String operator;
    private Date formTime;
    private Date toTime;
}
