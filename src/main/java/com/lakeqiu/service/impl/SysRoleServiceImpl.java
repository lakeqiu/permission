package com.lakeqiu.service.impl;

import com.google.common.collect.Lists;
import com.lakeqiu.common.RequestHolder;
import com.lakeqiu.exception.ParamException;
import com.lakeqiu.mapper.SysRoleAclMapper;
import com.lakeqiu.mapper.SysRoleMapper;
import com.lakeqiu.mapper.SysRoleUserMapper;
import com.lakeqiu.mapper.SysUserMapper;
import com.lakeqiu.model.SysRole;
import com.lakeqiu.model.SysUser;
import com.lakeqiu.service.SysRoleService;
import com.lakeqiu.utils.BeanValidator;
import com.lakeqiu.utils.IpUtil;
import com.lakeqiu.vo.RoleParam;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lakeqiu
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public void add(RoleParam roleParam) {
        BeanValidator.check(roleParam);

        // 检查数据库中是否已经存在当前角色
        if (checkExist(roleParam.getName(), roleParam.getId())) {
            throw new ParamException("存在相同的角色");
        }

        SysRole sysRole = SysRole.builder().name(roleParam.getName()).type(roleParam.getType())
                .status(roleParam.getStatus()).remark(roleParam.getRemark())
                .operator(RequestHolder.getCurrentUser().getUsername())
                .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                .operateTime(new Date()).build();

        sysRoleMapper.insertSelective(sysRole);
    }

    @Override
    public void update(RoleParam roleParam) {
        BeanValidator.check(roleParam);

        SysRole before = sysRoleMapper.selectByPrimaryKey(roleParam.getId());
        if (null == before) {
            throw new ParamException("待更新的角色不存在");
        }
        // 检查数据库中是否已经存在当前角色
        if (checkExist(roleParam.getName(), roleParam.getId())) {
            throw new ParamException("存在相同的角色");
        }

        SysRole sysRole = SysRole.builder().name(roleParam.getName()).type(roleParam.getType())
                .status(roleParam.getStatus()).remark(roleParam.getRemark())
                .operator(RequestHolder.getCurrentUser().getUsername())
                .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                .operateTime(new Date()).id(roleParam.getId()).build();

        sysRoleMapper.updateByPrimaryKeySelective(sysRole);
    }

    @Override
    public List<SysRole> getAllRole() {
        return sysRoleMapper.getAll();
    }

    /**
     * 根据用户id查询用户所扮演的角色
     *
     * @param userId 用户id
     * @return 角色列表
     */
    @Override
    public List<SysRole> getRoleListByUserId(Integer userId) {
        // 获取角色id
        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    /**
     * 根据权限点id获取相关角色列表
     *
     * @param aclId 权限点id
     * @return 角色列表
     */
    @Override
    public List<SysRole> getRoleListByAclId(Integer aclId) {
        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);
        // 检查是否为空
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    /**
     * 根据角色列表获取扮演这些角色的用户
     *
     * @param roleList 角色列表
     * @return 用户列表
     */
    @Override
    public List<SysUser> getUserListByRoleList(List<SysRole> roleList) {
        // 映射
        List<Integer> roleIdList = roleList.stream().map(SysRole::getId).collect(Collectors.toList());
        // 获取用户id列表
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }

        return sysUserMapper.getByIdList(userIdList);
    }

    private boolean checkExist(String name, Integer roleId) {
        return sysRoleMapper.countByName(name, roleId) > 0;
    }
}
