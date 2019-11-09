package com.lakeqiu.service.impl;

import com.lakeqiu.common.RequestHolder;
import com.lakeqiu.exception.ParamException;
import com.lakeqiu.mapper.SysRoleMapper;
import com.lakeqiu.model.SysRole;
import com.lakeqiu.service.SysRoleService;
import com.lakeqiu.utils.BeanValidator;
import com.lakeqiu.utils.IpUtil;
import com.lakeqiu.vo.RoleParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author lakeqiu
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public void add(RoleParam roleParam) {
        BeanValidator.check(roleParam);

        // 检查数据库中是否已经存在当前角色
        if (checkExist()) {
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
        if (checkExist()) {
            throw new ParamException("存在相同的角色");
        }

        SysRole sysRole = SysRole.builder().name(roleParam.getName()).type(roleParam.getType())
                .status(roleParam.getStatus()).remark(roleParam.getRemark())
                .operator(RequestHolder.getCurrentUser().getUsername())
                .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                .operateTime(new Date()).id(roleParam.getId()).build();

        sysRoleMapper.updateByPrimaryKeySelective(sysRole);
    }

    private boolean checkExist() {
        return false;
    }
}
