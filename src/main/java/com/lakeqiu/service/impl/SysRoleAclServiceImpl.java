package com.lakeqiu.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lakeqiu.common.RequestHolder;
import com.lakeqiu.mapper.SysRoleAclMapper;
import com.lakeqiu.model.SysRoleAcl;
import com.lakeqiu.service.SysLogService;
import com.lakeqiu.service.SysRoleAclService;
import com.lakeqiu.utils.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @author lakeqiu
 */
@Service
public class SysRoleAclServiceImpl implements SysRoleAclService {
    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SysLogService sysLogService;

    @Override
    public void changeRoleAcls(Integer roleId, List<Integer> aclIdList) {
        // 获取之前角色的权限点id
        List<Integer> beforeAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        // 判断权限点是否有改变
        if (aclIdList.size() == beforeAclIdList.size()) {
            HashSet<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            HashSet<Integer> beforeAclIdSet = Sets.newHashSet(beforeAclIdList);

            beforeAclIdSet.removeAll(aclIdSet);
            // 为空，说明权限点没有改变，不用修改数据库
            if (CollectionUtils.isEmpty(beforeAclIdSet)) {
                return;
            }
        }

        // 更新权限
        updateRoleAcls(roleId, aclIdList);

        sysLogService.svaeRoleAclLog(roleId, beforeAclIdList, aclIdList);
    }

    @Transactional
    void updateRoleAcls(Integer roleId, List<Integer> aclIdList) {
        // 删除旧的权限点
        sysRoleAclMapper.deleteByPrimaryKey(roleId);

        // 新权限点列表为空，不用往数据库增加了
        if (CollectionUtils.isEmpty(aclIdList)) {
            return;
        }
        // 遍历添加新权限
        aclIdList.forEach(aclId -> {
            SysRoleAcl roleAcl = SysRoleAcl.builder().aclId(aclId).roleId(roleId).operateTime(new Date())
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .build();

            sysRoleAclMapper.insertSelective(roleAcl);
        });
    }
}
