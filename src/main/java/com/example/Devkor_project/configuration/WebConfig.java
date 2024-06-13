package com.example.Devkor_project.configuration;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/*
    CORS 설정
*/

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry  registry) {
        registry
                .addMapping("/**")
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedOrigins("https://localhost:4000", "http://localhost:4000","https://localhost:3000", "http://localhost:3000", "https://127.0.0.1:4000",
                        "http://127.0.0.1:4000","https://127.0.0.1:3000",
                        "http://127.0.0.1:3000", "https://academ-frontend.vercel.app") // added more origin
                .allowedMethods("GET", "POST", "PUT", "DELETE"); // Wildcard method not works in some browsers, and HTTPS
    }

}
