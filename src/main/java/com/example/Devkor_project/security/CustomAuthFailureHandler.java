package com.example.Devkor_project.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.Devkor_project.configuration.VersionProvider;

import java.io.IOException;

@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    VersionProvider versionProvider;

    @Override

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String errorCode = null;

        if (exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException)
            errorCode = "WRONG"; // 이메일 또는 비밀번호가 틀린 경우
        else if (exception instanceof DisabledException)
            errorCode = "DISABLED"; // 해당 계정이 비활성화된 경우 (현재 사용 안함)
        else
            errorCode = "UNEXPECTED"; // 예기치 못한 에러가 발생한 경우

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().print("{\"status\":\"FAILURE\", \"reason\":\"" + errorCode + "\", \"version\":\""
                + versionProvider.getVersion() + "\"}");
        response.getWriter().flush();

        // super.onAuthenticationFailure(request, response, exception);
    }
}
