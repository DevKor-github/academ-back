package com.example.Devkor_project.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
        return httpSecurity
                .httpBasic(HttpBasicConfigurer::disable)    // HTTP 기본 인증 비활성화
                .csrf(CsrfConfigurer::disable)              // CSRF 보호 비활성화
                .cors(Customizer.withDefaults())            // CORS를 기본 값으로 활성화

                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/login/**").permitAll()   // 해당 요청은 모든 사용자에게 접근 권한 허용
                        .requestMatchers("/api/logout/**").permitAll()   // 해당 요청은 모든 사용자에게 접근 권한 허용
                        .requestMatchers("/api/signup/**").permitAll()   // 해당 요청은 모든 사용자에게 접근 권한 허용
                        .anyRequest().authenticated()   // 그 외의 요청은 인증된 사용자에게만 접근 권한 허용
                )

                // 필요시에 세션을 생성
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .build();
    }
}
