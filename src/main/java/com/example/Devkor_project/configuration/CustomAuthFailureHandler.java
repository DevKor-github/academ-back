package com.example.Devkor_project.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.ui.Model;

import java.io.IOException;

public class CustomAuthFailureHandler implements AuthenticationFailureHandler {
    private final String DEFAULT_FAILURE_URL = "/login?error";
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String ErrorMsg = null;

        if (exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException) {
            ErrorMsg = "Invalid username or password";
        } else if (exception instanceof DisabledException) {
            // User 비활성화 시 메시지 (현재 사용 안함)
        } else {
            ErrorMsg = "Unknown error. Please try again later.";
        }

        request.setAttribute("ErrorMsg", ErrorMsg);
        request.getRequestDispatcher(DEFAULT_FAILURE_URL).forward(request, response);
    }
}
