package com.lakeqiu.filter;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.lakeqiu.common.ApplicationContextHelper;
import com.lakeqiu.common.JsonData;
import com.lakeqiu.common.RequestHolder;
import com.lakeqiu.model.SysUser;
import com.lakeqiu.service.SysCoreService;
import com.lakeqiu.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lakeqiu
 */
@Slf4j
public class AclControlFilter implements Filter {
    /**
     * 存放过滤白名单的容器
     */
    private static Set<String> exclusionSet = Sets.newHashSet();
    /**
     * 没有权限访问时跳转路径
     */
    private final static String NOAUTHURL = "/sys/user/noAuth.page";
    /**
     * json请求
     */
    private final static String JSONREQUEST = ".json";

    /**
     * 初始化工作，获取拦截白名单
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
        // 分割成list
        List<String> exclusionList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        exclusionSet = Sets.newHashSet(exclusionList);
        // 将没有权限时的访问路径加进白名单中
        exclusionSet.add(NOAUTHURL);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 获取请求路径及用户参数
        String requestPath = request.getServletPath();
        Map parameterMap = request.getParameterMap();
        // 如果当前请求路径在白名单中，直接放行
        if (exclusionSet.contains(requestPath)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 请求路径不在白名单中
        // 在这里获取到了用户信息，这就是之前为什么存放用户信息方法放在过滤器中，而不放在HttpInterceptor中的原因了
        SysUser currentUser = RequestHolder.getCurrentUser();
        // 用户没有登录
        if (null == currentUser) {
            log.info("一些用户访问{}， 但没有登录，参数为{}", requestPath, JsonMapper.objToJson(parameterMap));
            // 拒绝本次访问
            noAuth(request, response);
            return;
        }

        // 用户已经登录，但没有访问权限
        // 这个过滤器不归spring管理，所以不能使用spring的依赖注入功能
        // 获取spring上下文的辅助类终于用上了
        SysCoreService sysCoreService = ApplicationContextHelper.getBean(SysCoreService.class);
        if (!sysCoreService.hasUrlAcl(requestPath)) {
            log.info("用户:{} 访问 {}， 但没有权限， 请求参数:{}", JsonMapper.objToJson(RequestHolder.getCurrentUser()),
                    requestPath, JsonMapper.objToJson(parameterMap));
            // 拒绝本次访问
            noAuth(request, response);
            return;
        }

        // 有权限访问，放行
        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    /**
     * 告诉用户没有权限访问
     * @param request
     * @param response
     * @throws IOException
     */
    private void noAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String servletPath = request.getServletPath();
        // 如果是json请求，将错误信息写回去
        if (servletPath.endsWith(JSONREQUEST)) {
            JsonData jsonData = JsonData.fail("没有权限访问，如需访问，请联系管理员");
            // 注明返回类型
            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(JsonMapper.objToJson(jsonData));
        } else {
            // 其他异常
            clientRedirect(NOAUTHURL, response);
        }
        return;

    }

    /**
     * 除json外其他没有权限该返回
     * @param url
     * @param response
     * @throws IOException
     */
    private void clientRedirect(String url, HttpServletResponse response) throws IOException{
        response.setHeader("Content-Type", "text/html");
        response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
                + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
                + "window.location.href='" + url + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");
    }


    @Override
    public void destroy() {

    }
}
