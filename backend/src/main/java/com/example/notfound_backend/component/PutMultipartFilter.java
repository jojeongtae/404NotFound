package com.example.notfound_backend.component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.io.IOException;

public class PutMultipartFilter implements Filter {

    private StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        if ("PUT".equalsIgnoreCase(req.getMethod()) && multipartResolver.isMultipart(req)) {
            MultipartHttpServletRequest multipartRequest = multipartResolver.resolveMultipart(req);
            chain.doFilter(multipartRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}