package com.lakeqiu.controller;

import com.lakeqiu.common.JsonData;
import com.lakeqiu.model.SysRole;
import com.lakeqiu.service.SysRoleAclService;
import com.lakeqiu.service.SysRoleService;
import com.lakeqiu.service.SysTreeService;
import com.lakeqiu.utils.StringUtil;
import com.lakeqiu.vo.RoleParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author lakeqiu
 */
@Controller
@RequestMapping("sys/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysTreeService sysTreeService;

    @Autowired
    private SysRoleAclService sysRoleAclService;

    @RequestMapping("role.page")
    public ModelAndView page() {
        return new ModelAndView("role");
    }

    /**
     * 添加角色
     * @param roleParam 角色信息
     * @return
     */
    @RequestMapping("save.json")
    @ResponseBody
    public JsonData add(RoleParam roleParam) {
        sysRoleService.add(roleParam);
        return JsonData.success();
    }

    /**
     * 更新角色
     * @param roleParam
     * @return
     */
    @RequestMapping("update.json")
    @ResponseBody
    public JsonData update(RoleParam roleParam) {
        sysRoleService.update(roleParam);
        return JsonData.success();
    }

    /**
     * 获取所有角色
     * @return
     */
    @RequestMapping("list.json")
    @ResponseBody
    public JsonData getAllRole() {
        List<SysRole> allRole = sysRoleService.getAllRole();
        return JsonData.success(allRole);
    }

    /**
     * 返回权限模块及权限点组成的树
     * @param roleId
     * @return
     */
    @RequestMapping("roleTree.json")
    @ResponseBody
    public JsonData roleTree(@RequestParam("roleId") int roleId) {
        return JsonData.success(sysTreeService.roleTree(roleId));
    }

    /**
     * 更新角色权限信息
     * @param roleId 角色id
     * @param aclIds 权限列表
     */
    @RequestMapping("changeAcls.json")
    @ResponseBody
    public JsonData changesAcls(@RequestParam("roleId") Integer roleId, @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
        List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
        sysRoleAclService.changeRoleAcls(roleId, aclIdList);
        return JsonData.success();
    }
}

