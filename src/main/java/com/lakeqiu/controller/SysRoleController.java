package com.lakeqiu.controller;

import com.lakeqiu.common.JsonData;
import com.lakeqiu.service.SysRoleService;
import com.lakeqiu.vo.RoleParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lakeqiu
 */
@Controller
@RequestMapping("sys/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping("save.json")
    @ResponseBody
    public JsonData add(RoleParam roleParam) {
        sysRoleService.add(roleParam);
        return JsonData.success();
    }

    @RequestMapping("update.json")
    @ResponseBody
    public JsonData update(RoleParam roleParam) {
        sysRoleService.update(roleParam);
        return JsonData.success();
    }
}
