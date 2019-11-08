package com.lakeqiu.service;

import com.lakeqiu.beans.PageQuery;
import com.lakeqiu.beans.PageResult;
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

    /**
     * 根据电话或邮箱查询用户
     * @param username
     * @return
     */
    SysUser findByKeyword(String username);

    /**
     * 分页查询部门用户
     * @param deptId 部门id
     * @param pageQuery 分页请求
     */
    PageResult<SysUser> getPageByDeptId(Integer deptId, PageQuery pageQuery);
}
