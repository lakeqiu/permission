package com.lakeqiu.dto;

import com.lakeqiu.model.SysAcl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * @author lakeqiu
 */
@Getter
@Setter
@ToString
public class AclDto extends SysAcl {
    /**
     * 默认是否需要选中
     */
    private boolean checked = false;

    /**
     * 是否有权限操作
     * 这里会把所有权限列给用户，当一些权限用户是没有操作权限的
     * 这样做是为了让管理员能够看到所有权限，方便管理
     */
    private boolean hasAcl = false;

    public static AclDto adapt(SysAcl sysAcl) {
        AclDto aclDto = new AclDto();
        BeanUtils.copyProperties(sysAcl, aclDto);
        return aclDto;
    }
}
