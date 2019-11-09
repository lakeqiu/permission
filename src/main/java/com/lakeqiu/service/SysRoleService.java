package com.lakeqiu.service;

import com.lakeqiu.model.SysRole;
import com.lakeqiu.vo.RoleParam;

import java.util.List;

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

    /**
     * 获取所有角色
     * @return 角色列表
     */
    List<SysRole> getAllRole();
}
