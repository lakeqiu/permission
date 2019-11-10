package com.lakeqiu.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lakeqiu.common.RequestHolder;
import com.lakeqiu.mapper.SysRoleUserMapper;
import com.lakeqiu.mapper.SysUserMapper;
import com.lakeqiu.model.SysRoleUser;
import com.lakeqiu.model.SysUser;
import com.lakeqiu.service.SysLogService;
import com.lakeqiu.service.SysRoleUserService;
import com.lakeqiu.utils.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lakeqiu
 */
@Service
public class SysRoleUserServiceImpl implements SysRoleUserService {
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysLogService sysLogService;

    @Override
    public List<SysUser> getUsersByRoleId(Integer roleId) {
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        // 根据用户id查询出用户
        return sysUserMapper.getByIdList(userIdList);
    }

    /**
     * 根据角色id更改用户扮演的角色
     *
     * @param roleId     角色id
     * @param userIdList 用户列表
     */
    @Override
    public void changeRoleUsers(int roleId, List<Integer> userIdList) {
        // 1、先查询出扮演这个角色的用户，看一下用户列表是否相同
        List<Integer> beforeUserList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (beforeUserList.size() == userIdList.size()) {
            HashSet<Integer> beforeUserSet = Sets.newHashSet(beforeUserList);
            HashSet<Integer> userIdSet = Sets.newHashSet(userIdList);

            beforeUserSet.removeAll(userIdSet);

            // 里面用户id列表一样，直接返回
            if (CollectionUtils.isEmpty(beforeUserSet)) {
                return;
            }
        }

        // 2、不相同，删除这种角色用户的关系，再添加新的角色用户关系
        updateRoleUsers(roleId, userIdList);

        sysLogService.saveRoleUserLog(roleId, beforeUserList, userIdList);

    }

    /**
     * 事务删除以前扮演这个角色的用户并添加新的扮演这个角色的用户
     * @param roleId 角色id
     * @param userIdList 用户id列表
     */
    @Transactional
    void updateRoleUsers(Integer roleId, List<Integer> userIdList) {
        sysRoleUserMapper.deleteByRoleId(roleId);

        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }

        // 转化为SysRoleUser
        List<SysRoleUser> roleUsers = userIdList.stream().
                map(uerId -> SysRoleUser.builder().roleId(roleId).userId(uerId)
                        .operator(RequestHolder.getCurrentUser().getUsername())
                        .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                        .operateTime(new Date()).build())
                .collect(Collectors.toList());

        // 批量插入
        sysRoleUserMapper.batchInsert(roleUsers);
    }
}
