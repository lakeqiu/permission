package com.lakeqiu.controller;

import com.google.common.collect.Maps;
import com.lakeqiu.beans.PageQuery;
import com.lakeqiu.beans.PageResult;
import com.lakeqiu.common.JsonData;
import com.lakeqiu.model.SysUser;
import com.lakeqiu.service.SysRoleService;
import com.lakeqiu.service.SysTreeService;
import com.lakeqiu.service.SysUserService;
import com.lakeqiu.vo.UserParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author lakeqiu
 */
@Controller
@RequestMapping("sys/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysTreeService sysTreeService;

    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping("save.json")
    @ResponseBody
    public JsonData addUser(UserParam userParam) {
        sysUserService.addUser(userParam);
        return JsonData.success("添加用户成功");
    }

    @RequestMapping("update.json")
    @ResponseBody
    public JsonData updateUser(UserParam userParam) {
        sysUserService.updateUser(userParam);
        return JsonData.success("更新用户成功");
    }

    @RequestMapping("page.json")
    @ResponseBody
    public JsonData page(@RequestParam("deptId") Integer deptId, PageQuery pageQuery) {
        PageResult<SysUser> pageResult = sysUserService.getPageByDeptId(deptId, pageQuery);
        return JsonData.success(pageResult);
    }

    /**
     * 根据用户id查询出用户的权限树级角色
     * @param userId
     * @return
     */
    @RequestMapping("acls.json")
    @ResponseBody
    public JsonData acls(@RequestParam("userId") int userId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", sysTreeService.userAclTree(userId));
        map.put("roles", sysRoleService.getRoleListByUserId(userId));
        return JsonData.success(map);
    }
}
