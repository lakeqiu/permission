package com.lakeqiu.service;

import com.lakeqiu.model.SysRole;
import com.lakeqiu.model.SysUser;
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

    /**
     * 根据用户id查询用户所扮演的角色
     * @param userId 用户id
     * @return 角色列表
     */
    List<SysRole> getRoleListByUserId(Integer userId);

    /**
     * 根据权限点id获取相关角色列表
     * @param aclId 权限点id
     * @return 角色列表
     */
    List<SysRole> getRoleListByAclId(Integer aclId);

    /**
     * 根据角色列表获取扮演这些角色的用户
     * @param roleList 角色列表
     * @return 用户列表
     */
    List<SysUser> getUserListByRoleList(List<SysRole> roleList);
}
