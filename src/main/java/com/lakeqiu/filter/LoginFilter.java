package com.lakeqiu.filter;

import com.lakeqiu.common.RequestHolder;
import com.lakeqiu.model.SysUser;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lakeqiu
 */
@Slf4j
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        // 检查用户是否登录
        SysUser user = (SysUser) req.getSession().getAttribute("user");
        // 没有登录，跳转登录页面
        if (null == user) {
            // 这里如果不加/的话就会默认从当前路径跳转，会导致路径错误（/admin/signin.jsp）
            String path = "/signin.jsp";
            resp.sendRedirect(path);
            return;
        }
        // 登录了，将相关信息取出来，放到线程容器中，方便后面使用
        RequestHolder.add(user);
        RequestHolder.add(req);

        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    @Override
    public void destroy() {

    }
}
