package com.lakeqiu.service;

import com.lakeqiu.dto.AclModuleLevelDto;
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

    /**
     * 将所有权限模块转化为树形结构
     * @return
     */
    List<AclModuleLevelDto> aclModuleTree();

    /**
     * 将所有权限模块以及权限点转化为树形结构，
     * 并标出当前角色拥有的权限
     * @param roleId 角色id
     * @return
     */
    List<AclModuleLevelDto> roleTree(Integer roleId);
}
