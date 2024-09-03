package com.example.Devkor_project.configuration;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// CORS 설정

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedOrigins(
                        "https://localhost:4000",
                        "http://localhost:4000",
                        "https://localhost:3000",
                        "http://localhost:3000",
                        "http://localhost",
                        "https://localhost",
                        "https://academ-frontend.vercel.app",
                        "https://13.124.249.107.nip.io",
                        "https://academ.kr",
                        "https://academ.devkor.club"
                )   // CORS 허용 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE"); // CORS 허용 메서드
    }

}
