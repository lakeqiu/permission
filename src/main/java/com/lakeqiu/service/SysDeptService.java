package com.lakeqiu.service;

import com.lakeqiu.vo.DeptParam;

/**
 * @author lakeqiu
 */
public interface SysDeptService {
    /**
     * 添加部门
     * @param deptParam 部门信息
     */
    void saveDept(DeptParam deptParam);

    /**
     * 更新部门信息
     * @param deptParam 部门信息
     */
    void updateDept(DeptParam deptParam);
}
