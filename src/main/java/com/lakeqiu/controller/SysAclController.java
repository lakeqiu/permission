package com.lakeqiu.controller;

import com.lakeqiu.beans.PageQuery;
import com.lakeqiu.beans.PageResult;
import com.lakeqiu.common.JsonData;
import com.lakeqiu.model.SysAcl;
import com.lakeqiu.service.SysAclService;
import com.lakeqiu.vo.AclParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lakeqiu
 */
@Controller
@RequestMapping("sys/acl")
public class SysAclController {
    @Autowired
    private SysAclService sysAclService;

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

    @RequestMapping("page.json")
    @ResponseBody
    public JsonData page(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery pageQuery) {
        PageResult<SysAcl> pageResult = sysAclService.getPageByAclModuleId(aclModuleId, pageQuery);
        return JsonData.success(pageResult);
    }
}
