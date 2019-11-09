package com.lakeqiu.service.impl;

import com.lakeqiu.beans.PageQuery;
import com.lakeqiu.beans.PageResult;
import com.lakeqiu.common.RequestHolder;
import com.lakeqiu.exception.ParamException;
import com.lakeqiu.mapper.SysAclMapper;
import com.lakeqiu.model.SysAcl;
import com.lakeqiu.service.SysAclService;
import com.lakeqiu.utils.BeanValidator;
import com.lakeqiu.utils.IpUtil;
import com.lakeqiu.vo.AclParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author lakeqiu
 */
@Service
public class SysAclServiceImpl implements SysAclService {
    @Autowired
    private SysAclMapper sysAclMapper;

    @Override
    public void add(AclParam aclParam) {
        BeanValidator.check(aclParam);

        // 检查一下要更新的权限点存不存在
        SysAcl before = sysAclMapper.selectByPrimaryKey(aclParam.getId());
        if (null == before) {
            throw new ParamException("待更新的权限点不存在");
        }

        // 检查当前权限点在数据库中是否存在
        if (checkExist(aclParam.getAclModuleId(), aclParam.getName(), aclParam.getId())) {
            throw new ParamException("存在相同的权限点");
        }

        SysAcl sysAcl = SysAcl.builder().name(aclParam.getName()).aclModuleId(aclParam.getAclModuleId())
                .url(aclParam.getUrl()).type(aclParam.getType())
                .status(aclParam.getStatus()).seq(aclParam.getSeq())
                .remark(aclParam.getRemark()).operateTime(new Date())
                .operator(RequestHolder.getCurrentUser().getUsername())
                .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                .code(generateCode()).build();

        sysAclMapper.insertSelective(sysAcl);

    }

    @Override
    public void update(AclParam aclParam) {
        BeanValidator.check(aclParam);

        // 检查当前权限点在数据库中是否存在
        if (checkExist(aclParam.getAclModuleId(), aclParam.getName(), aclParam.getId())) {
            throw new ParamException("存在相同的权限点");
        }

        SysAcl sysAcl = SysAcl.builder().id(aclParam.getId()).aclModuleId(aclParam.getAclModuleId())
                .url(aclParam.getUrl()).type(aclParam.getType())
                .status(aclParam.getStatus()).seq(aclParam.getSeq())
                .remark(aclParam.getRemark()).operateTime(new Date())
                .operator(RequestHolder.getCurrentUser().getUsername())
                .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                .name(aclParam.getName()).build();

        sysAclMapper.updateByPrimaryKeySelective(sysAcl);
    }

    @Override
    public PageResult<SysAcl> getPageByAclModuleId(Integer aclModuleId, PageQuery pageQuery) {
        BeanValidator.check(pageQuery);

        // 查询出该权限模块下所有权限点
        int count = sysAclMapper.countByAclModuleId(aclModuleId);
        if (count > 0) {
            List<SysAcl> sysAclList = sysAclMapper.getPageByAclModuleId(aclModuleId, pageQuery);
            return PageResult.<SysAcl>builder().data(sysAclList).total(count).build();
        }
        return PageResult.<SysAcl>builder().build();
    }

    private boolean checkExist(Integer aclModuleId, String aclName, Integer aclId) {
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, aclName, aclId) > 0;
    }

    /**
     * 生成权限点状态码
     * @return 权限点状态码
     */
    private String generateCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date() + "_" + (int)(Math.random() * 100));
    }
}
