package com.example.Devkor_project.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler
{
    @Autowired VersionProvider versionProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws ServletException, IOException {

        ResponseDto.Error dto = ResponseDto.Error.builder()
                .code("LOW_AUTHORITY")
                .message("권한이 부족합니다.")
                .data(null)
                .version(versionProvider.getVersion())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(dto));
        response.getWriter().flush();

    }
}
