package com.lakeqiu.service;

import com.lakeqiu.beans.PageQuery;
import com.lakeqiu.beans.PageResult;
import com.lakeqiu.model.SysAcl;
import com.lakeqiu.vo.AclParam;

/**
 * @author lakeqiu
 */
public interface SysAclService {
    /**
     * 添加权限点
     * @param aclParam 权限点参数
     */
    void add(AclParam aclParam);

    /**
     * 更新权限点
     * @param aclParam 权限点参数
     */
    void update(AclParam aclParam);

    /**
     * 根据权限模块id分页获取权限点
     * @param aclModuleId 权限所属权限模块
     * @param pageQuery 分页请求参数
     * @return 分页结果
     */
    PageResult<SysAcl> getPageByAclModuleId(Integer aclModuleId, PageQuery pageQuery);
}
