package com.lakeqiu.service;

import com.lakeqiu.vo.AclModuleParam;

/**
 * @author lakeqiu
 */
public interface SysAclModuleService {
    /**
     * 添加权限模块
     * @param aclModuleParam 权限模块信息
     */
    void add(AclModuleParam aclModuleParam);

    /**
     * 更新权限信息
     * @param aclModuleParam 权限模块信息
     */
    void update(AclModuleParam aclModuleParam);

    /**
     * 删除权限模块
     * @param aclModuleId 权限模块id
     */
    void delete(Integer aclModuleId);
}
