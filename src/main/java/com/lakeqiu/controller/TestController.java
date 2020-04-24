package com.lakeqiu.controller;

import com.lakeqiu.common.ApplicationContextHelper;
import com.lakeqiu.common.JsonData;
import com.lakeqiu.model.SysAcl;
import com.lakeqiu.utils.BeanValidator;
import com.lakeqiu.utils.JsonMapper;
import com.lakeqiu.vo.TestVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lakeqiu
 */
@Controller
@RequestMapping("test")
@Slf4j
public class TestController {

    @RequestMapping("hello.json")
    @ResponseBody
    public JsonData test(){
        /*SysAcl bean = ApplicationContextHelper.getBean(SysAcl.class);
        bean.setAclModuleId(1);
        log.info(JsonMapper.objToJson(bean));*/
        // throw new PermissionException("权限不足");
        return JsonData.success("hello");

    }

    @RequestMapping("validate.json")
    @ResponseBody
    public JsonData validate(TestVo vo){
        log.info("validate.json");
        BeanValidator.check(vo);
        return JsonData.success("接收数据成功");
    }
}
