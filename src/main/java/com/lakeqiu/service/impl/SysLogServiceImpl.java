package com.lakeqiu.service.impl;

import com.lakeqiu.beans.LogType;
import com.lakeqiu.common.RequestHolder;
import com.lakeqiu.mapper.SysLogMapper;
import com.lakeqiu.model.*;
import com.lakeqiu.service.SysLogService;
import com.lakeqiu.utils.IpUtil;
import com.lakeqiu.utils.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author lakeqiu
 */
@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;

    /**
     * 保存部门更新记录
     *
     * @param before 更新前的部门
     * @param after  更新后的部门
     */
    @Override
    public void saveDeptLog(SysDept before, SysDept after) {
        SysLogWithBLOBs logWithBLOBs = new SysLogWithBLOBs();
        logWithBLOBs.setType(LogType.TYPE_DEPT);
        logWithBLOBs.setTargetId(null == after ? before.getId() : after.getId());
        logWithBLOBs.setOldValue(null == before? "" : JsonMapper.objToJson(before));
        logWithBLOBs.setNewValue(null == after? "" : JsonMapper.objToJson(after));
        logWithBLOBs.setOperateTime(new Date());
        logWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        logWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        logWithBLOBs.setStatus(1);

        sysLogMapper.insertSelective(logWithBLOBs);
    }

    /**
     * 保存用户更新记录
     *
     * @param before
     * @param after
     */
    @Override
    public void saveUserLog(SysUser before, SysUser after) {
        SysLogWithBLOBs logWithBLOBs = new SysLogWithBLOBs();
        logWithBLOBs.setType(LogType.TYPE_USER);
        logWithBLOBs.setTargetId(null == after ? before.getId() : after.getId());
        logWithBLOBs.setOldValue(null == before? "" : JsonMapper.objToJson(before));
        logWithBLOBs.setNewValue(null == after? "" : JsonMapper.objToJson(after));
        logWithBLOBs.setOperateTime(new Date());
        logWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        logWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        logWithBLOBs.setStatus(1);

        sysLogMapper.insertSelective(logWithBLOBs);
    }

    /**
     * 保存权限模块更新记录
     *
     * @param before
     * @param after
     */
    @Override
    public void saveAclModuleLog(SysAclModule before, SysAclModule after) {
        SysLogWithBLOBs logWithBLOBs = new SysLogWithBLOBs();
        logWithBLOBs.setType(LogType.TYPE_ACL_MODULE);
        logWithBLOBs.setTargetId(null == after ? before.getId() : after.getId());
        logWithBLOBs.setOldValue(null == before? "" : JsonMapper.objToJson(before));
        logWithBLOBs.setNewValue(null == after? "" : JsonMapper.objToJson(after));
        logWithBLOBs.setOperateTime(new Date());
        logWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        logWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        logWithBLOBs.setStatus(1);

        sysLogMapper.insertSelective(logWithBLOBs);
    }

    /**
     * 保存权限点更新记录
     *
     * @param before
     * @param after
     */
    @Override
    public void saveAclLog(SysAcl before, SysAcl after) {
        SysLogWithBLOBs logWithBLOBs = new SysLogWithBLOBs();
        logWithBLOBs.setType(LogType.TYPE_ACL);
        logWithBLOBs.setTargetId(null == after ? before.getId() : after.getId());
        logWithBLOBs.setOldValue(null == before? "" : JsonMapper.objToJson(before));
        logWithBLOBs.setNewValue(null == after? "" : JsonMapper.objToJson(after));
        logWithBLOBs.setOperateTime(new Date());
        logWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        logWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        logWithBLOBs.setStatus(1);

        sysLogMapper.insertSelective(logWithBLOBs);
    }

    /**
     * 保存角色更新记录
     *
     * @param before
     * @param after
     */
    @Override
    public void saveRoleLog(SysRole before, SysRole after) {
        SysLogWithBLOBs logWithBLOBs = new SysLogWithBLOBs();
        logWithBLOBs.setType(LogType.TYPE_ROLE);
        logWithBLOBs.setTargetId(null == after ? before.getId() : after.getId());
        logWithBLOBs.setOldValue(null == before? "" : JsonMapper.objToJson(before));
        logWithBLOBs.setNewValue(null == after? "" : JsonMapper.objToJson(after));
        logWithBLOBs.setOperateTime(new Date());
        logWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        logWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        logWithBLOBs.setStatus(1);

        sysLogMapper.insertSelective(logWithBLOBs);
    }

    /**
     * 保存角色权限关系更新记录
     *
     * @param roleId
     * @param before
     * @param after
     */
    @Override
    public void svaeRoleAclLog(Integer roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs logWithBLOBs = new SysLogWithBLOBs();
        logWithBLOBs.setType(LogType.TYPE_ROLE_ACL);
        logWithBLOBs.setTargetId(roleId);
        logWithBLOBs.setOldValue(null == before? "" : JsonMapper.objToJson(before));
        logWithBLOBs.setNewValue(null == after? "" : JsonMapper.objToJson(after));
        logWithBLOBs.setOperateTime(new Date());
        logWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        logWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        logWithBLOBs.setStatus(1);

        sysLogMapper.insertSelective(logWithBLOBs);
    }

    /**
     * 保存角色用户关系更新记录
     *
     * @param roleId
     * @param before
     * @param after
     */
    @Override
    public void saveRoleUserLog(Integer roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs logWithBLOBs = new SysLogWithBLOBs();
        logWithBLOBs.setType(LogType.TYPE_ROLE_USER);
        logWithBLOBs.setTargetId(roleId);
        logWithBLOBs.setOldValue(null == before? "" : JsonMapper.objToJson(before));
        logWithBLOBs.setNewValue(null == after? "" : JsonMapper.objToJson(after));
        logWithBLOBs.setOperateTime(new Date());
        logWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        logWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        logWithBLOBs.setStatus(1);

        sysLogMapper.insertSelective(logWithBLOBs);
    }
}
