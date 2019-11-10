package com.lakeqiu.service.impl;

import com.google.common.collect.Lists;
import com.lakeqiu.common.RequestHolder;
import com.lakeqiu.mapper.SysAclMapper;
import com.lakeqiu.mapper.SysRoleAclMapper;
import com.lakeqiu.mapper.SysRoleUserMapper;
import com.lakeqiu.model.SysAcl;
import com.lakeqiu.model.SysUser;
import com.lakeqiu.service.SysCoreService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        // 这里是我自己定义了一个假的超级管理员规则，实际中要根据项目进行修改
        // 可以是配置文件获取，可以指定某个用户，也可以指定某个角色
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser.getMail().contains("admin")) {
            return true;
        }
        return false;
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

    /**
     * 判断当前用户是否有这个路径的访问权限
     *
     * @param servletPath 路径
     * @return 结果
     */
    @Override
    public boolean hasUrlAcl(String servletPath) {
        if (isSuperAdmin()) {
            return true;
        }

        // 根据url查询出其权限点
        List<SysAcl> sysAclList = sysAclMapper.getByUrl(servletPath);
        // 如果没有这个权限点，说明我们根本不关心这个请求路径，可以访问
        if (CollectionUtils.isEmpty(sysAclList)) {
            return true;
        }
        // 获取当前用户的所有权限点
        List<SysAcl> currentUserAclList = getCurrentUserAclList();
        Set<Integer> currentUserAclSet = currentUserAclList.stream().map(SysAcl::getId).collect(Collectors.toSet());

        // 判断是否有有效权限点
        boolean hasValidAcl = false;
        // 遍历可以访问这个路径的权限点
        for (SysAcl sysAcl : sysAclList) {
            // 这个权限无效
            if (null == sysAcl || sysAcl.getStatus() != 1) {
                continue;
            }
            hasValidAcl = true;
            // 有权限访问
            if (currentUserAclSet.contains(sysAcl.getId())) {
                return true;
            }
        }
        // 如果没有有效权限点，那么这个路径等于不需要权限访问了，返回true
        if (!hasValidAcl) {
            return true;
        }
        return false;


        /*// 获取当前登录用户
        SysUser currentUser = RequestHolder.getCurrentUser();
        // 获取用户扮演的角色id
        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(currentUser.getId());
        // 查询出这些角色拥有的权限
        List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(roleIdList);
        // 获取用户的所有权限点
        List<SysAcl> aclList = sysAclMapper.getByIdList(aclIdList);
        // 检查当中有没有访问的路径
        return aclList.stream().map(SysAcl::getUrl).anyMatch(url -> url.equals(servletPath));*/
    }
}
