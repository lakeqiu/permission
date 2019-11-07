package com.lakeqiu.service;

import com.lakeqiu.model.SysUser;
import com.lakeqiu.vo.UserParam;

/**
 * @author lakeqiu
 */
public interface SysUserService {
    /**
     * 新增用户
     * @param userParam 用户传递对象
     */
    void addUser(UserParam userParam);

    /**
     * 更新用户信息
     * @param userParam 用户信息
     */
    void updateUser(UserParam userParam);

    SysUser findByKeyword(String username);
}
