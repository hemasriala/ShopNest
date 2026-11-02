package com.shopnest.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/** Sends users to the login page if they open a protected page without logging in. */
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (request.getSession().getAttribute("userId") == null) {
            response.sendRedirect("/login?required");
            return false;
        }
        return true;
    }
}
