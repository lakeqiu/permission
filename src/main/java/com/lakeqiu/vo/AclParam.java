package com.lakeqiu.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author lakeqiu
 */
@Getter
@Setter
@ToString
public class AclParam {
    private Integer id;

    @NotBlank(message = "权限点名称不能为空")
    @Length(min = 2, max = 20, message = "权限点名称应在2~20个字之间")
    private String name;

    @NotNull(message = "必须指定权限模块")
    private Integer aclModuleId;

    @Length(min = 4, max = 256, message = "权限点url长度应在4~256个字")
    private String url;

    @NotNull(message = "权限点类型不能为空")
    @Max(value = 2, message = "权限点类型不合法")
    @Min(value = 0, message = "权限点类型不合法")
    private Integer type;

    @NotNull(message = "权限点状态不能为空")
    @Max(value = 1, message = "权限点状态不合法")
    @Min(value = 0, message = "权限点状态不合法")
    private Integer status;

    @NotNull(message = "必须指定权限点的展示顺序")
    private Integer seq;

    @Length(min = 0, max = 256, message = "权限点备注长度应在256个字之内")
    private String remark = "";
}
