package com.lakeqiu.common;

import com.lakeqiu.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author lakeqiu
 */
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {
    private final static String START_TIME = "requestStartTime";

    /**
     * 请求处理之前调用这个方法
     * true表示不拦截
     * false表示拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        Map map = request.getParameterMap();
        log.info("preHandler -> url:{}, params:{}", url, JsonMapper.objToJson(map));
        // 记录请求到来时间
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME, startTime);
        return true;
    }

    /**
     * 请求正常结束时之后调用这个方法
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String url = request.getRequestURL().toString();
        Map map = request.getParameterMap();
        log.info("postHandler -> url:{}, params:{}", url, JsonMapper.objToJson(map));
        Long startTime = (Long) request.getAttribute(START_TIME);
        long endTime = System.currentTimeMillis();
        log.info("{} -> {}ms", url, endTime - startTime);
    }

    /**
     * 请求结束后调用这个方法（正常结束，异常结束都会调用这个方法）
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURL().toString();
        Map map = request.getParameterMap();
        log.info("afterCompletion -> url:{}, params:{}", url, JsonMapper.objToJson(map));
        Long startTime = (Long) request.getAttribute(START_TIME);
        long endTime = System.currentTimeMillis();
        log.info("{} -> {}", url, endTime - startTime);
    }
}
