package com.robin.ws.webserviceexample.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.time.Instant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class Interceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(Interceptor.class);

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        long startTime = Instant.now().toEpochMilli();
        if (request.getUserPrincipal() != null) {
            logger.info("Request URL::" + request.getRequestURL().toString() +
                    ":: Start Time=" + Instant.now() + ":: USER=" + request.getUserPrincipal().getName());
            request.setAttribute("startTime", startTime);
        }
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {
        if (request != null) {
            long startTime = (Long) request.getAttribute("startTime");
            logger.info("Request URL::" + request.getRequestURL().toString() +
                    ":: Time Taken=" + (Instant.now().toEpochMilli() - startTime));
        }


    }

}
