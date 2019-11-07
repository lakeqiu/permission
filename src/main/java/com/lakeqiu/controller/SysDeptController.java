package com.lakeqiu.controller;

import com.lakeqiu.common.JsonData;
import com.lakeqiu.dto.DeptLevelDto;
import com.lakeqiu.service.SysDeptService;
import com.lakeqiu.service.SysTreeService;
import com.lakeqiu.utils.BeanValidator;
import com.lakeqiu.vo.DeptParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author lakeqiu
 */
@Controller
@RequestMapping("sys/dept")
public class SysDeptController {
    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysTreeService sysTreeService;

    @RequestMapping("dept.page")
    public ModelAndView page() {
        return new ModelAndView("dept");
    }

    @RequestMapping("save.json")
    @ResponseBody
    public JsonData saveDept(DeptParam deptParam){
        sysDeptService.saveDept(deptParam);
        return JsonData.success("添加部门成功");
    }

    @RequestMapping("tree.json")
    @ResponseBody
    public JsonData deptTree(){
        List<DeptLevelDto> deptTree = sysTreeService.deptTree();
        return JsonData.success(deptTree);
    }

    @RequestMapping("update.json")
    @ResponseBody
    public JsonData updateDept(DeptParam deptParam){
        sysDeptService.updateDept(deptParam);
        return JsonData.success("更新部门成功");
    }
}
