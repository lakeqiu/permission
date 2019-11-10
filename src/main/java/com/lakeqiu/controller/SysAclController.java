package com.lakeqiu.controller;

import com.google.common.collect.Maps;
import com.lakeqiu.beans.PageQuery;
import com.lakeqiu.beans.PageResult;
import com.lakeqiu.common.JsonData;
import com.lakeqiu.model.SysAcl;
import com.lakeqiu.model.SysRole;
import com.lakeqiu.service.SysAclService;
import com.lakeqiu.service.SysRoleService;
import com.lakeqiu.vo.AclParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author lakeqiu
 */
@Controller
@RequestMapping("sys/acl")
public class SysAclController {
    @Autowired
    private SysAclService sysAclService;

    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping("save.json")
    @ResponseBody
    public JsonData add(AclParam aclParam) {
        sysAclService.add(aclParam);
        return JsonData.success();
    }

    @RequestMapping("update.json")
    @ResponseBody
    public JsonData update(AclParam aclParam) {
        sysAclService.update(aclParam);
        return JsonData.success();
    }

    /**
     * 根据权限模块id分页获取权限点
     * @param aclModuleId 权限模块id
     * @param pageQuery 分页请求
     * @return 分页结果
     */
    @RequestMapping("page.json")
    @ResponseBody
    public JsonData page(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery pageQuery) {
        PageResult<SysAcl> pageResult = sysAclService.getPageByAclModuleId(aclModuleId, pageQuery);
        return JsonData.success(pageResult);
    }

    /**
     * 根据权限点id获取用户与角色列表
     * @param aclId 权限点id
     * @return map
     */
    @RequestMapping("acls.json")
    @ResponseBody
    public JsonData acls(@RequestParam("aclId") int aclId) {
        Map<String, Object> map = Maps.newHashMap();
        // 获取角色
        List<SysRole> roleList = sysRoleService.getRoleListByAclId(aclId);
        map.put("roles", roleList);
        // 获取用户
        map.put("users", sysRoleService.getUserListByRoleList(roleList));
        return JsonData.success(map);
    }
}
