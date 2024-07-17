package com.example.filter.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LoggerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        log.info(">>>>>진입전");

        /*var req = new HttpServletRequestWrapper((HttpServletRequest) servletRequest);
        var res = new HttpServletResponseWrapper((HttpServletResponse) servletResponse);

        var br = req.getReader();

        var list = br.lines().collect(Collectors.toList());

        list.forEach(it -> {
            log.info("{}",it);
        });*/

        var req = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        var res = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        filterChain.doFilter(req, res);

        var reqJson = new String(req.getContentAsByteArray());
        log.info("req:{}",reqJson);

        var resJson = new String(res.getContentAsByteArray());
        log.info("res:{}", resJson);

        log.info("<<<<<진입후 리턴");

        res.copyBodyToResponse();
    }
}

