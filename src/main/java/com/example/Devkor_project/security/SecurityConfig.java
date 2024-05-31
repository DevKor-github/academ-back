package com.example.Devkor_project.security;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CustomUserDetailsService customUserDetailsService) throws Exception
    {
            httpSecurity
                .httpBasic(HttpBasicConfigurer::disable)    // HTTP 기본 인증 비활성화
                            .csrf(CsrfConfigurer::disable);             // CSRF 보호 비활성화

                            httpSecurity
                        //     .requiresChannel(channel -> channel
                        //         .anyRequest().requiresSecure()
                        //                     )
                             .headers(headers -> headers
                             .httpStrictTransportSecurity(hsts -> hsts
                             .includeSubDomains(true)
                             .maxAgeInSeconds(31536000) // 1년
                             )
                     )
                .authorizeHttpRequests((requests) -> (requests)
                        // 아무나 접근 가능
                        .requestMatchers("/", "/login", "/signup").permitAll()
                        .requestMatchers("/api/login/**", "/api/signup/**").permitAll()
                        // USER 또는 ADMIN 계정만 접근 가능
                        .requestMatchers("/search/**", "/api/search/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/course/**", "/api/course/**").hasAnyRole("USER", "ADMIN")
                        // ADMIN 계정만 접근 가능
                        .requestMatchers("/api/admin/**", "/admin/**").hasRole("ADMIN")
                        // 그 외의 요청은 모든 사용자에게 접근 권한 허용
                        .anyRequest().authenticated()
                        );

                
                

        httpSecurity
                .formLogin((auth) -> auth
                        .usernameParameter("email") // email 변경 예정
                        .passwordParameter("password")
                        .loginPage("/login")  // frontend login page
                        .permitAll()
                        .loginProcessingUrl("/api/login")   // post api
                        .permitAll()
                        .defaultSuccessUrl("/", true) // success 시 direct
                        .failureHandler(customAuthFailureHandler())
                        );


        httpSecurity
                .logout((logoutConfig) -> logoutConfig
                        .logoutUrl("/logout")
                        .addLogoutHandler((request, response, authentication) -> {
                            HttpSession session = request.getSession();
                            if (session != null) {
                                session.invalidate();
                            }
                        })
                        .logoutSuccessUrl("/")
                        .deleteCookies("remember-me")
                );


        httpSecurity
                .rememberMe((rememberConfig) -> rememberConfig
                        .key("Test-Key-For-Academ")
                        .tokenValiditySeconds(60 * 60 * 24 * 30) // 30일
                        .rememberMeParameter("remember-me")
                        .userDetailsService(customUserDetailsService)
                );

        // 필요시에 세션을 생성
        httpSecurity
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .maximumSessions(1).maxSessionsPreventsLogin(false)
                        // 1명만 로그인 가능 & 다른 기기 로그인 시 기존 사용자 세션 만료
                ).sessionManagement(sessionManagement ->
                        sessionManagement.sessionFixation().newSession()
                        // 로그인 시 새로운 세션 발행 (보안)
                );

        return httpSecurity.build();
    }

    @Bean
    public CustomAuthFailureHandler customAuthFailureHandler() {
        return new CustomAuthFailureHandler();
    }
}
