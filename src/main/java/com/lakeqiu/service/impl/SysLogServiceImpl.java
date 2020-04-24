package com.lakeqiu.service.impl;

import com.google.common.base.Preconditions;
import com.lakeqiu.beans.LogType;
import com.lakeqiu.beans.PageQuery;
import com.lakeqiu.beans.PageResult;
import com.lakeqiu.common.RequestHolder;
import com.lakeqiu.dto.SearchLogDto;
import com.lakeqiu.exception.ParamException;
import com.lakeqiu.mapper.*;
import com.lakeqiu.model.*;
import com.lakeqiu.service.SysLogService;
import com.lakeqiu.service.SysRoleAclService;
import com.lakeqiu.service.SysRoleUserService;
import com.lakeqiu.utils.BeanValidator;
import com.lakeqiu.utils.IpUtil;
import com.lakeqiu.utils.JsonMapper;
import com.lakeqiu.vo.SearchLogParam;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author lakeqiu
 */
@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;

    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleAclService sysRoleAclService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    /*@Override
    public void recover(int id) {
        SysLogWithBLOBs sysLog = sysLogMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(sysLog, "待还原的记录不存在");
        switch (sysLog.getType()){
            case LogType.TYPE_DEPT:
                SysDept beforeDept = sysDeptMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeDept, "待还原的部门已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue())  || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
                SysDept afterDept = JsonMapper.jsonToObj(sysLog.getOldValue(), new TypeReference<SysDept>() {
                });
                afterDept.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterDept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterDept.setOperateTime(new Date());
                sysDeptMapper.updateByPrimaryKeySelective(afterDept);
                saveDeptLog(beforeDept, afterDept);
                break;
            case LogType.TYPE_USER:
                SysUser beforeUser = sysUserMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeUser, "待还原的用户已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue())  || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
                SysUser afterUser = JsonMapper.jsonToObj(sysLog.getOldValue(), new TypeReference<SysUser>() {
                });
                afterUser.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterUser.setOperateTime(new Date());
                sysUserMapper.updateByPrimaryKeySelective(afterUser);
                saveUserLog(beforeUser, afterUser);
                break;
            case LogType.TYPE_ACL_MODULE:
                SysAclModule beforeAclModule = sysAclModuleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeAclModule, "待还原的权限模块已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue())  || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
                SysAclModule afterAclModule = JsonMapper.jsonToObj(sysLog.getOldValue(), new TypeReference<SysAclModule>() {
                });
                afterAclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterAclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterAclModule.setOperateTime(new Date());
                sysAclModuleMapper.updateByPrimaryKeySelective(afterAclModule);
                saveAclModuleLog(beforeAclModule, afterAclModule);
                break;
            case LogType.TYPE_ACL:
                SysAcl beforeAcl = sysAclMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeAcl, "待还原的权限点已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue())  || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
                SysAcl afterAcl = JsonMapper.jsonToObj(sysLog.getOldValue(), new TypeReference<SysAcl>() {
                });
                afterAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterAcl.setOperateTime(new Date());
                sysAclMapper.updateByPrimaryKeySelective(afterAcl);
                saveAclLog(beforeAcl, afterAcl);
                break;
            case LogType.TYPE_ROLE:
                SysRole beforeRole = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeRole, "待还原的角色已经不存在了");
                if (StringUtils.isBlank(sysLog.getNewValue())  || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
                SysRole afterRole = JsonMapper.jsonToObj(sysLog.getOldValue(), new TypeReference<SysRole>() {
                });
                afterRole.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterRole.setOperateTime(new Date());
                sysRoleMapper.updateByPrimaryKeySelective(afterRole);
                saveRoleLog(beforeRole, afterRole);
                break;
            case LogType.TYPE_ROLE_ACL:
                SysRole aclRole = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(aclRole, "角色已经不存在了");
                sysRoleAclService.changeRoleAcls(sysLog.getTargetId(), JsonMapper.jsonToObj(sysLog.getOldValue(), new TypeReference<List<Integer>>() {
                }));
                break;
            case LogType.TYPE_ROLE_USER:
                SysRole userRole = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(userRole, "角色已经不存在了");
                sysRoleUserService.changeRoleUsers(sysLog.getTargetId(), JsonMapper.jsonToObj(sysLog.getOldValue(), new TypeReference<List<Integer>>() {
                }));
                break;
            default:;
        }
    }*/

    @Override
    public PageResult<SysLogWithBLOBs> searchPageList(SearchLogParam param, PageQuery page) {
        BeanValidator.check(page);
        SearchLogDto dto = new SearchLogDto();
        dto.setType(param.getType());
        if (StringUtils.isNotBlank(param.getBeforeSeg())) {
            dto.setBeforeSeg("%" + param.getBeforeSeg() + "%");
        }
        if (StringUtils.isNotBlank(param.getAfterSeg())) {
            dto.setAfterSeg("%" + param.getAfterSeg() + "%");
        }
        if (StringUtils.isNotBlank(param.getOperator())) {
            dto.setOperator("%" + param.getOperator() + "%");
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isNotBlank(param.getFormTime())) {
                dto.setFormTime(dateFormat.parse(param.getFormTime()));
            }
            if (StringUtils.isNotBlank(param.getToTime())) {
                dto.setToTime(dateFormat.parse(param.getToTime()));
            }
        } catch (Exception e) {
            throw new ParamException("传入的日期格式有问题，正确格式为：yyyy-MM-dd HH:mm:ss");
        }
        int count = sysLogMapper.countBySearchDto(dto);
        if (count > 0){
            List<SysLogWithBLOBs> logList = sysLogMapper.getPageListBySearchDto(dto, page);
            return PageResult.<SysLogWithBLOBs>builder().total(count).data(logList).build();
        }
        return PageResult.<SysLogWithBLOBs>builder().build();
    }


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

    /**
     * 权限还原
     *
     * @param id
     */
    @Override
    public void recover(int id) {

    }
}
