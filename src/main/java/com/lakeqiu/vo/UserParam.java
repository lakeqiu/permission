package com.lakeqiu.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author lakeqiu
 */
@Getter
@Setter
@ToString
public class UserParam {
    private Integer id;

    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, min = 1, message = "用户名在20个字以内")
    private String username;

    @NotBlank(message = "手机不能为空")
    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机不符合规范")
    private String telephone;

    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = "^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$", message = "邮箱不符合规范")
    private String mail;

    @NotNull(message = "必须提供用户所在部门")
    private Integer deptId;

    @NotNull(message = "必须指定用户的状态")
    @Min(value = 0, message = "用户状态不合法")
    @Max(value = 2, message = "用户状态不合法")
    private Integer status;

    @Length(max = 200, min = 0, message = "备注长度要在200字以内")
    private String remark = "";
}
