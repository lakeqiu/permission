package com.lakeqiu.service;

import com.lakeqiu.model.SysUser;

import java.util.List;

/**
 * @author lakeqiu
 */
public interface SysRoleUserService {
    /**
     * 根据角色id获取扮演这个角色的用户列表
     * @param roleId
     * @return
     */
    List<SysUser> getUsersByRoleId(Integer roleId);

    /**
     * 根据角色id更改用户扮演的角色
     * @param roleId 角色id
     * @param userIdList 用户列表
     */
    void changeRoleUsers(int roleId, List<Integer> userIdList);
}
