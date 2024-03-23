package com.hust.filter;

import com.alibaba.fastjson.JSONObject;

import com.hust.utils.JwtUtils;
import com.hust.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@WebFilter(urlPatterns = "/normal/*")
public class LoginCheckFilter implements Filter {
    /*  可以点那个更多操作，可以自己导入的*/
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String url = req.getRequestURL().toString();
        log.info("请求路径：{}", url);
        //放行
        if (url.contains("login")) {
            filterChain.doFilter(req, resp);
            return;
        }
        String token = req.getHeader("token");
        log.info("从请求头中获取的令牌：{}", token);
        if (!StringUtils.hasLength(token)) {
            log.info("Token不存在");
            Result responseResult = Result.error("NOT_LOGIN");
            String json = JSONObject.toJSONString(responseResult);
            servletResponse.setContentType("application/json;charset=utf-8");
            servletResponse.getWriter().write(json);
            return;
        }
        try {
            JwtUtils.parseJWT(token);
        } catch (Exception e) {
            log.info("令牌解析失败！");
            Result responseResult = Result.error("NOT_LOGIN");
            String json = JSONObject.toJSONString(responseResult);
            servletResponse.setContentType("application/json;charset=utf-8");
            servletResponse.getWriter().write(json);
            return;
        }

        filterChain.doFilter(req, resp);
    }
}

