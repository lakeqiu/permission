package com.lakeqiu.controller;

import com.lakeqiu.common.JsonData;
import com.lakeqiu.dto.AclModuleLevelDto;
import com.lakeqiu.service.SysAclModuleService;
import com.lakeqiu.service.SysTreeService;
import com.lakeqiu.vo.AclModuleParam;
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
@RequestMapping("sys/aclModule")
public class SysAclModuleController {
    @Autowired
    private SysAclModuleService sysAclModuleService;

    @Autowired
    private SysTreeService sysTreeService;

    @RequestMapping("acl.page")
    public ModelAndView page() {
        return new ModelAndView("acl");
    }

    @RequestMapping("save.json")
    @ResponseBody
    public JsonData saveAclModule(AclModuleParam param) {
        sysAclModuleService.add(param);
        return JsonData.success();
    }

    @RequestMapping("update.json")
    @ResponseBody
    public JsonData updateAclModule(AclModuleParam param) {
        sysAclModuleService.update(param);
        return JsonData.success();
    }

    @RequestMapping("tree.json")
    @ResponseBody
    public JsonData aclModuleTree() {
        List<AclModuleLevelDto> aclModuleTree = sysTreeService.aclModuleTree();
        return JsonData.success(aclModuleTree);
    }

    @RequestMapping("delete.json")
    @ResponseBody
    public JsonData delete(@RequestParam("id") Integer id) {
        sysAclModuleService.delete(id);
        return JsonData.success();
    }
}
