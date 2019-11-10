package com.lakeqiu.service;

import com.lakeqiu.model.*;

import java.util.List;

/**
 * @author lakeqiu
 */
public interface SysLogService {
    /**
     * 保存部门更新记录
     * @param before 更新前的部门
     * @param after 更新后的部门
     */
    void saveDeptLog(SysDept before, SysDept after);

    /**
     * 保存用户更新记录
     * @param before
     * @param after
     */
    void saveUserLog(SysUser before, SysUser after);

    /**
     * 保存权限模块更新记录
     * @param before
     * @param after
     */
    void saveAclModuleLog(SysAclModule before, SysAclModule after);

    /**
     * 保存权限点更新记录
     * @param before
     * @param after
     */
    void saveAclLog(SysAcl before, SysAcl after);

    /**
     * 保存角色更新记录
     * @param before
     * @param after
     */
    void saveRoleLog(SysRole before, SysRole after);

    /**
     * 保存角色权限关系更新记录
     * @param roleId
     * @param before 之前的id
     * @param after
     */
    void svaeRoleAclLog(Integer roleId, List<Integer> before, List<Integer> after);

    /**
     * 保存角色用户关系更新记录
     * @param roleId
     * @param before
     * @param after
     */
    void saveRoleUserLog(Integer roleId, List<Integer> before, List<Integer> after);
}
