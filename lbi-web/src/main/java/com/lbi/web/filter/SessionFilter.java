package com.lbi.web.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * Created by lmk on 2017/3/25.
 */
public class SessionFilter extends OncePerRequestFilter {
    //过滤标记
    private static final String FILTERED_REQUEST="@@session_context_filtered_request";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)throws ServletException, IOException {
        //保证该过滤器在一次请求中只被调用一次
        if(request !=null && request.getAttribute(FILTERED_REQUEST)!=null){
            doFilter(request,response,chain);
        }else{
            //设置过滤标识，防止一次请求多次过滤
            request.setAttribute(FILTERED_REQUEST, Boolean.TRUE);
            doFilter(request,response,chain);
        }

    }
    private void doFilter(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)throws ServletException, IOException{
        if(isGzipSupport(request)){
            GZipResponseWrapper gzipRes = new GZipResponseWrapper(response);
            chain.doFilter(request, gzipRes);
            gzipRes.finishResponse();//输出压缩数据
        }else{
            chain.doFilter(request, response);
        }
    }
    /**
     * 判断浏览器是否支持 gzip 压缩
     * @param req
     * @return boolean 值
     */
    private boolean isGzipSupport(HttpServletRequest req) {
        String headEncoding = req.getHeader("accept-encoding");
        if (headEncoding == null || (headEncoding.indexOf("gzip") == -1)) { // 客户端 不支持 gzip
            return false;
        } else { // 支持 gzip 压缩
            return true;
        }
    }

}
