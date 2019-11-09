package com.lakeqiu.service;

import java.util.List;

/**
 * @author lakeqiu
 */
public interface SysRoleAclService {
    /**
     * 更新角色权限点信息
     * @param roleId 角色id
     * @param aclIdList 权限列表
     */
    void changeRoleAcls(Integer roleId, List<Integer> aclIdList);
}
