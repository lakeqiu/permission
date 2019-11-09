package com.lakeqiu.service;

import com.lakeqiu.vo.RoleParam;

/**
 * @author lakeqiu
 */
public interface SysRoleService {

    /**
     * 新增角色
     * @param roleParam 角色信息
     */
    void add(RoleParam roleParam);

    /**
     * 更新角色
     * @param roleParam 更新角色
     */
    void update(RoleParam roleParam);
}
