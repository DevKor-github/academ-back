package com.example.Devkor_project.security;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                        CustomUserDetailsService customUserDetailsService) throws Exception {
                httpSecurity
                                .httpBasic(HttpBasicConfigurer::disable) // HTTP 기본 인증 비활성화
                                .csrf(CsrfConfigurer::disable); // CSRF 보호 비활성화

                httpSecurity
                                .authorizeHttpRequests((requests) -> (requests)
                                                // 아무나 접근 가능
                                                .requestMatchers("/").permitAll()
                                                .requestMatchers("/api/login/**", "/api/signup/**").permitAll()
                                                .requestMatchers("/api/login/check-login")
                                                .hasAnyRole("USER", "ADMIN")
                                                .requestMatchers("/api/search/**")
                                                .hasAnyRole("USER", "ADMIN")
                                                .requestMatchers("/api/course/**")
                                                .hasAnyRole("USER", "ADMIN") // ADMIN 계정만 접근 가능
                                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                                // 그 외의 요청은 모든 사용자에게 접근 권한 허용
                                                .anyRequest().permitAll())

                                .exceptionHandling((exception) -> exception
                                                .accessDeniedHandler(customAccessDeniedHandler()));

                httpSecurity
                                .formLogin((auth) -> auth
                                                .usernameParameter("email") // email 변경 예정
                                                .passwordParameter("password")
                                                .loginPage("/login") // frontend login page
                                                .permitAll()
                                                .loginProcessingUrl("/api/login") // post api
                                                .permitAll()
                                                .successHandler(customAuthSuccessHandler())
                                                .failureHandler(customAuthFailureHandler()));

                httpSecurity
                                .logout((logoutConfig) -> logoutConfig
                                                .logoutUrl("/api/logout")
                                                .addLogoutHandler((request, response, authentication) -> {
                                                        HttpSession session = request.getSession();
                                                        if (session != null) {
                                                                session.invalidate();
                                                        }
                                                })
                                                .logoutSuccessHandler(customLogoutSuccessHandler())
                                                .deleteCookies("remember-me"));

                httpSecurity
                                .rememberMe((rememberConfig) -> rememberConfig
                                                .key("Test-Key-For-Academ")
                                                .tokenValiditySeconds(60 * 60 * 24 * 30) // 30일
                                                .rememberMeParameter("remember-me")
                                                .userDetailsService(customUserDetailsService));

                // 필요시에 세션을 생성
                httpSecurity
                                .sessionManagement(sessionManagement -> sessionManagement
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                                .maximumSessions(1).maxSessionsPreventsLogin(false)
                                // 1명만 로그인 가능 & 다른 기기 로그인 시 기존 사용자 세션 만료
                                )
                                .sessionManagement(sessionManagement -> sessionManagement.sessionFixation().newSession()
                                // 로그인 시 새로운 세션 발행 (보안)
                                );

                return httpSecurity.build();
        }

        @Bean
        public CustomAccessDeniedHandler customAccessDeniedHandler() {
                return new CustomAccessDeniedHandler();
        }

        @Bean
        public CustomAuthSuccessHandler customAuthSuccessHandler() {
                return new CustomAuthSuccessHandler();
        }

        @Bean
        public CustomAuthFailureHandler customAuthFailureHandler() {
                return new CustomAuthFailureHandler();
        }

        @Bean
        public CustomLogoutSuccessHandler customLogoutSuccessHandler() {
                return new CustomLogoutSuccessHandler();
        }
}
