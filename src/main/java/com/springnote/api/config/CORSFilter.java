package com.springnote.api.config;

import io.grpc.netty.shaded.io.netty.handler.codec.http.HttpMethod;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Order(-1)
@Component
public class CORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        var request = (HttpServletRequest) req;
        var response = (HttpServletResponse) res;

        var origin = (request.getHeader("Origin") != null || !request.getHeader("Origin").isBlank()) ? request.getHeader("Origin") : "localhost";
        response.setHeader("Access-Control-Allow-Origin", "*"); // * = all domainName
        response.setHeader("Access-Control-Allow-Credentials", "true"); // allow CrossDomain to use Origin Domain
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600"); // Preflight cache duration in browser
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
        response.setHeader("Access-Control-Allow-Headers", origin); // all header


        chain.doFilter(req, res);
    }
}
