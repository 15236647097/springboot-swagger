package com.neo;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**跨域的设置问题
 * Created by Administrator on 2017/11/23.
 */
@Order(1)
//@Component
//@ServletComponentScan
@WebFilter(urlPatterns = "/*",filterName = "ACAFilter")
public class ACAFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        //response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", ":x-requested-with,content-type");
        //((HttpServletResponse)servletResponse).setHeader("Access-Control-Allow-Origin", "*");
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("to access control allow origin");
    }

    @Override
    public void destroy() {
    }
}
