package com.lbi.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

/**
 * Created by lmk on 2017/3/25.
 */
@Slf4j
public class LogNDCInteceptor extends HandlerInterceptorAdapter {
    Long star = 0l;
    Long end = 0l;
    /*
	 * 最后执行，可用于释放资源
	 */
    @Override
    @SuppressWarnings("unchecked")
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex)
            throws Exception {
        end = System.currentTimeMillis(); // 结束时间
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

        MDC.put("ip", remoteAddr);
        MDC.put("duration", String.valueOf (end - star));
        MDC.put("method", method);
        log.info(url);
        super.afterCompletion(request, response, handler, ex);
    }

    /*
     * 生成视图之前执行
     */
    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    /*
     * Action之前执行
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        star = System.currentTimeMillis();// 开始时间
        return super.preHandle(request, response, handler);
    }

    public String getRemortIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null)
            return request.getRemoteAddr();
        return request.getHeader("x-forwarded-for");
    }
}
