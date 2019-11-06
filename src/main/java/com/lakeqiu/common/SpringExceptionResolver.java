package com.lakeqiu.common;

import com.lakeqiu.exception.ParamException;
import com.lakeqiu.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HandlerExceptionResolver是spring mvc中用来处理程序的异常，包括 Handler 映射、数据绑定以及目标方法执行时发生的异常。
 * 这里我们使用其来处理所有异常
 * @author lakeqiu
 */
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {
    private final String JSON_REQ = ".json";
    private final String PAGE_REQ = ".page";


    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        String url = httpServletRequest.getRequestURL().toString();
        ModelAndView mv;
        // 默认未知异常
        String defaultMsg = "System error";
        JsonData result;

        // 如果是json请求
        if (url.endsWith(JSON_REQ)){
            if (e instanceof PermissionException || e instanceof ParamException){
                result = JsonData.fail(e.getMessage());
                mv = new ModelAndView("jsonView", result.toMap());
            }else {
                log.error("未知json错误, url -> ", url, e);
                result = JsonData.fail(defaultMsg);
                mv = new ModelAndView("jsonView", result.toMap());
            }
        }else if (url.endsWith(PAGE_REQ)){ // 如果是page请求
            log.error("未知page错误, url -> ", url, e);
            result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("jsonView", result.toMap());
        }else {
            log.error("未知错误, url -> ", url, e);
            result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("jsonView", result.toMap());
        }

        return mv;
    }
}
