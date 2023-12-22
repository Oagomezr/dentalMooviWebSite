package com.dentalmoovi.website.security.jwt;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTentryPoint implements AuthenticationEntryPoint{
    @Override
    //this method controls the error when the user doesn't have any authorizations
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException {
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }
}
