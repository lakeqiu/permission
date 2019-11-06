package com.lakeqiu.vo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lakeqiu
 */
@Getter
@Setter
public class TestVo {
    @NotBlank(message = "名字不能为空")
    private String name;

    @NotNull(message = "id不能为空")
    @Min(value = 0, message = "id不能为负数")
    private Integer age;

    @NotEmpty
    private List<String> list;
}
