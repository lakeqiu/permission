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
public class RoleParam {

    private Integer id;

    @NotBlank(message = "角色名称不能为空")
    @Length(min = 2, max = 20, message = "角色名称应在2~20个字之间")
    private String name;

    @Max(value = 2, message = "角色类型不合法")
    @Min(value = 1, message = "角色类型不合法")
    private Integer type = 1;

    @NotNull(message = "必须指定角色的展示顺序")
    private Integer status;

    @Length(min = 0, max = 256, message = "角色备注长度应在256个字之内")
    private String remark;
}
