package com.lakeqiu.service.impl;

import com.lakeqiu.common.RequestHolder;
import com.lakeqiu.exception.ParamException;
import com.lakeqiu.mapper.SysAclMapper;
import com.lakeqiu.mapper.SysAclModuleMapper;
import com.lakeqiu.model.SysAclModule;
import com.lakeqiu.service.SysAclModuleService;
import com.lakeqiu.service.SysLogService;
import com.lakeqiu.utils.BeanValidator;
import com.lakeqiu.utils.IpUtil;
import com.lakeqiu.utils.LevelUtil;
import com.lakeqiu.vo.AclModuleParam;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author lakeqiu
 */
@Service
public class SysAclModuleServiceImpl implements SysAclModuleService {
    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;

    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysLogService sysLogService;

    @Override
    public void add(AclModuleParam aclModuleParam) {
        BeanValidator.check(aclModuleParam);

        if (checkExist(aclModuleParam.getParentId(), aclModuleParam.getName(), aclModuleParam.getId())) {
            throw new ParamException("存在相同的权限模块");
        }

        SysAclModule sysAclModule = SysAclModule.builder().name(aclModuleParam.getName())
                .parentId(aclModuleParam.getParentId())
                .seq(aclModuleParam.getSeq()).status(aclModuleParam.getStatus())
                .remark(aclModuleParam.getRemark()).operateTime(new Date())
                .operator(RequestHolder.getCurrentUser().getUsername())
                .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                .build();

        // 计算当前权限层级关系
        sysAclModule.setLevel(LevelUtil.calculateLevel(getLevel(aclModuleParam.getParentId()), aclModuleParam.getParentId()));

        sysAclModuleMapper.insertSelective(sysAclModule);

        sysLogService.saveAclModuleLog(null, sysAclModule);
    }

    @Override
    public void update(AclModuleParam aclModuleParam) {
        BeanValidator.check(aclModuleParam);

        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(aclModuleParam.getId());
        if (null == before) {
            throw new ParamException("待更新的权限模块不存在");
        }
        if (checkExist(aclModuleParam.getParentId(), aclModuleParam.getName(), aclModuleParam.getId())) {
            throw new ParamException("存在相同的权限模块");
        }

        SysAclModule after = SysAclModule.builder().name(aclModuleParam.getName())
                .parentId(aclModuleParam.getParentId()).id(aclModuleParam.getId())
                .seq(aclModuleParam.getSeq()).status(aclModuleParam.getStatus())
                .remark(aclModuleParam.getRemark()).operateTime(new Date())
                .operator(RequestHolder.getCurrentUser().getUsername())
                .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                .build();
        // 计算当前权限层级关系
        after.setLevel(LevelUtil.calculateLevel(getLevel(aclModuleParam.getParentId()), aclModuleParam.getParentId()));

        // 更新权限模块及子权限模块
        updateWithChild(before, after);

        sysLogService.saveAclModuleLog(before, after);
    }

    @Transactional
    void updateWithChild(SysAclModule before, SysAclModule after) {
        String beforeLevel = before.getLevel();
        String afterLevel = after.getLevel();

        // 检查子权限模块是否需要更新
        if (!beforeLevel.equals(afterLevel)) {
            // 需要更新，查出所有子权限模块
            List<SysAclModule> aclModuleList = sysAclModuleMapper.getChildAclModuleListByLevel(beforeLevel);
            // 子权限模块不为空
            if (CollectionUtils.isNotEmpty(aclModuleList)) {
                aclModuleList.stream().filter(a -> a.getLevel().indexOf(beforeLevel) == 0)
                        .forEach(a -> a.setLevel(afterLevel + a.getLevel().substring(beforeLevel.length())));
            }

            // 更新子权限模块
            sysAclModuleMapper.batchUpdateLevel(aclModuleList);
        }

        // 更新权限模块
        sysAclModuleMapper.updateByPrimaryKeySelective(after);

    }

    private boolean checkExist(Integer parentId, String aclModuleName, Integer aclModuleId) {
        return sysAclModuleMapper.countByNameAndParentId(parentId, aclModuleName, aclModuleId) > 0;
    }

    /**
     * 获取传入权限模块的层级
     * @param aclModuleId 权限模块信息
     * @return 权限模块的层级
     */
    private String getLevel(Integer aclModuleId) {
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        return null == sysAclModule ? null : sysAclModule.getLevel();
    }

    /**
     * 删除权限模块
     *
     * @param aclModuleId 权限模块id
     */
    @Override
    public void delete(Integer aclModuleId) {
        // 检查有没有这个权限模块
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (null == sysAclModule) {
            throw new ParamException("当前权限模块不存在");
        }

        // 查看这个权限模块有没有子权限模块或权限点
        if (sysAclModuleMapper.countByParentId(aclModuleId) > 0) {
            throw new ParamException("当前权限模块下有子权限模块，不能删除");
        }
        if (sysAclMapper.countByAclModuleId(aclModuleId) > 0) {
            throw new ParamException("当前权限模块下有权限点，不能删除");
        }

        // 删除部门
        sysAclModuleMapper.deleteByPrimaryKey(aclModuleId);

        sysLogService.saveAclModuleLog(sysAclModule, null);
    }
}
