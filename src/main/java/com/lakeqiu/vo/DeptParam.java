package com.lakeqiu.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author lakeqiu
 */
@Getter
@Setter
@ToString
public class DeptParam {
    private Integer id;

    @NotBlank(message = "部门名字不能为空")
    @Length(max = 12, min = 2, message = "部门名字程度应该在2~12字")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "部门顺序不能为空")
    private Integer seq;

    @Length(max = 150, message = "备注应该在150个字内")
    private String remark;
}
