package de.blaumeise03.projectmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoggingInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Processed request from " + request.getRemoteAddr() + " to: " + request.getRequestURI());
        logger.debug("Request cookies: " + request.getHeader("cookie"));
        logger.debug("Response Set-Cookie: " + response.getHeaders("set-cookie"));
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
