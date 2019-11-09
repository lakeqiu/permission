package com.lakeqiu.service;

import com.lakeqiu.model.SysAcl;

import java.util.List;

/**
 * 权限
 * @author lakeqiu
 */
public interface SysCoreService {
    /**
     * 获取当前用户权限列表
     * @return 权限列表
     */
    List<SysAcl> getCurrentUserAclList();

    /**
     * 获取指定用户权限列表
     * @param userId 指定用户id
     * @return 权限列表
     */
    List<SysAcl> getUserAclListByUserId(Integer userId);

    /**
     * 获取指定角色权限列表
     * @param roleId 指定角色id
     * @return 权限列表
     */
    List<SysAcl> getRoleAclListByRoleId(Integer roleId);
}
