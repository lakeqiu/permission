package com.lakeqiu.service.impl;

import com.google.common.collect.Lists;
import com.lakeqiu.common.RequestHolder;
import com.lakeqiu.mapper.SysAclMapper;
import com.lakeqiu.mapper.SysRoleAclMapper;
import com.lakeqiu.mapper.SysRoleUserMapper;
import com.lakeqiu.model.SysAcl;
import com.lakeqiu.service.SysCoreService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lakeqiu
 */
@Service
public class SysCoreServiceImpl implements SysCoreService {
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SysAclMapper sysAclMapper;

    @Override
    public List<SysAcl> getCurrentUserAclList() {
        // 获取当前用户id并查询其权限列表
        return getUserAclListByUserId(RequestHolder.getCurrentUser().getId());
    }

    @Override
    public List<SysAcl> getUserAclListByUserId(Integer userId) {
        // 1、检查用户是否是超级管理员
        // 为什么要这样？在系统刚运行时，大家都没有权限，这时候怎么分配权限？
        // 这时候，就需要超级管理员，其可以操纵所有权限，让其去分配权限
        if (isSuperAdmin()) {
            // 如果是的话，直接返回所有权限
            return sysAclMapper.getAll();
        }

        // 2、先查询出当前用户所扮演的角色列表id
        List<Integer> roleList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        // 如果用户没有扮演任何角色，不用往下查了，直接返回空列表
        if (CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }
        // 3、查询出上面所查询出来的角色所拥有的权限点id
        List<Integer> aclList = sysRoleAclMapper.getAclIdListByRoleIdList(roleList);
        // 如果角色没有任何权限，不用往下查了，直接返回空列表
        if (CollectionUtils.isEmpty(aclList)) {
            return Lists.newArrayList();
        }
        // 4、查询出上面查出来的所有权限点id所对应的权限详情
        return sysAclMapper.getByIdList(aclList);
    }

    /**
     * 检查是否是超级管理员
     * @return 结果
     */
    private boolean isSuperAdmin() {
        return true;
    }

    @Override
    public List<SysAcl> getRoleAclListByRoleId(Integer roleId) {
        // 1、查询出角色id所拥有的权限点id
        List<Integer> aclList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        // 如果角色没有任何权限，不用往下查了，直接返回空列表
        if (CollectionUtils.isEmpty(aclList)) {
            return Lists.newArrayList();
        }

        // 2、查询出上面查出来的所有权限点id所对应的权限详情
        return sysAclMapper.getByIdList(aclList);
    }
}
