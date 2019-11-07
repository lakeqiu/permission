package com.lakeqiu.service;

import com.lakeqiu.dto.DeptLevelDto;

import java.util.List;

/**
 * @author lakeqiu
 */
public interface SysTreeService {
    /**
     * 获取所有部门并转化为树形结构
     * @return
     */
    List<DeptLevelDto> deptTree();
}
