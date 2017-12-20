package com.lbi.tile.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

import static org.apache.commons.lang3.StringUtils.isNoneEmpty;
@Order(2)
@WebFilter(filterName = "sessionFilter", urlPatterns = "/*")
@Slf4j
public class SessionFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        long star = System.currentTimeMillis();
        HttpServletRequest request=(HttpServletRequest)req;
        String remoteAddr = getRemortIP(request);
        String queryString = request.getQueryString();
        String method = request.getMethod();
        String url = request.getRequestURI();
        if (method.equalsIgnoreCase("POST")) {// 遍历post参数
            Enumeration<String> e = request.getParameterNames();
            String parameterName, parameterValue;
            while (e.hasMoreElements()) {
                parameterName = e.nextElement();
                parameterValue = request.getParameter(parameterName);
                url += "&" + parameterName + "=" + parameterValue;
            }
            url=url.replaceAll("'", "\"");
        } else {
            if (isNoneEmpty(queryString)
                    && method.equalsIgnoreCase("GET")) {
                url += "&" + queryString;
            }
            url = url.replaceFirst("&", "?");
        }

        chain.doFilter(req, res);
        long end = System.currentTimeMillis();
        MDC.put("ip", remoteAddr);
        MDC.put("duration", String.valueOf (end - star));
        MDC.put("method", method);
        log.info(url);
    }
    public void init(FilterConfig filterConfig) {}

    public void destroy() {}

    private String getRemortIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) return request.getRemoteAddr();
        else return request.getHeader("x-forwarded-for");
    }
}
