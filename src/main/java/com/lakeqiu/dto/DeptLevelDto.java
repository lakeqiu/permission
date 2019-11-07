package com.lakeqiu.dto;

import com.google.common.collect.Lists;
import com.lakeqiu.model.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 层级树对象
 * @author lakeqiu
 */
@Getter
@Setter
@ToString
public class DeptLevelDto extends SysDept {
    /**
     * 存放其下一层的元素
     */
    private List<DeptLevelDto> dtoList = Lists.newArrayList();

    /**
     * 将SysDept转换为DeptLevelDto
     * @param sysDept
     * @return
     */
    public static DeptLevelDto adapt(SysDept sysDept) {
        DeptLevelDto deptLevelDto = new DeptLevelDto();
        // 将SysDept全部复制到DeptLevelDto的相应属性中
        BeanUtils.copyProperties(sysDept, deptLevelDto);
        return deptLevelDto;
    }
}
