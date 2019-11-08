package com.lakeqiu.controller;

import com.lakeqiu.beans.PageQuery;
import com.lakeqiu.beans.PageResult;
import com.lakeqiu.common.JsonData;
import com.lakeqiu.model.SysUser;
import com.lakeqiu.service.SysUserService;
import com.lakeqiu.vo.UserParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lakeqiu
 */
@Controller
@RequestMapping("sys/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

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

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(@RequestParam("deptId") Integer deptId, PageQuery pageQuery) {
        PageResult<SysUser> pageResult = sysUserService.getPageByDeptId(deptId, pageQuery);
        return JsonData.success(pageResult);
    }
}
