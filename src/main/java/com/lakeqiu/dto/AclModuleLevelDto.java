package com.lakeqiu.dto;

import com.google.common.collect.Lists;
import com.lakeqiu.model.SysAclModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author lakeqiu
 */
@Getter
@Setter
@ToString
public class AclModuleLevelDto extends SysAclModule {
    /**
     * 存放子权限模块
     */
    List<AclModuleLevelDto> aclModuleList = Lists.newArrayList();

    List<AclDto> aclDtoList = Lists.newArrayList();

    /**
     * 将sysAclModule转化为SysAclModuleDto
     * @param sysAclModule
     * @return
     */
    public static AclModuleLevelDto adapt(SysAclModule sysAclModule) {
        AclModuleLevelDto sysAclModuleDto = new AclModuleLevelDto();
        BeanUtils.copyProperties(sysAclModule, sysAclModuleDto);
        return sysAclModuleDto;
    }

}
