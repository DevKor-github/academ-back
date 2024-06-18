package com.example.Devkor_project.security;

import jakarta.servlet.http.HttpSession;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                       CustomUserDetailsService customUserDetailsService) throws Exception {
                httpSecurity
                        .httpBasic(HttpBasicConfigurer::disable)    // HTTP 기본 인증 비활성화
                        .csrf(CsrfConfigurer::disable);             // CSRF 보호 비활성화

                httpSecurity
                        .authorizeHttpRequests((requests) -> (requests)
                                // 아무나 접근 가능
                                .requestMatchers("/", "/login", "/signup", "logout").permitAll()
                                .requestMatchers("/api/login/**", "/api/signup/**", "/api/logout").permitAll()
                                .requestMatchers("/search/**", "/api/search/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/login/check-login").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/course/**", "/api/course/**").hasAnyRole("USER", "ADMIN") // ADMIN 계정만 접근 가능
                                .requestMatchers("/api/admin/**", "/admin/**").hasRole("ADMIN")
                                // 그 외의 요청은 모든 사용자에게 접근 권한 허용
                                .anyRequest().permitAll())

                        .exceptionHandling((exception) -> exception
                                .accessDeniedHandler(customAccessDeniedHandler()));

                httpSecurity
                        .formLogin((auth) -> auth
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .loginPage("/login")
                                .permitAll()
                                .loginProcessingUrl("/api/login")
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

                httpSecurity
                        .sessionManagement((session) -> session
                                .sessionFixation().changeSessionId()                            // 로그인 시, 기존 세션 무효화
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)       // 필요시에 세션을 생성
                                .maximumSessions(1)                                             // 1명만 로그인 가능
                                .maxSessionsPreventsLogin(false)                                // 다른 기기 로그인 시 기존 사용자 세션 만료
                                .sessionRegistry(sessionRegistry())                             // 동시에 로그인한 세션들 추적
                                .expiredSessionStrategy(customSessionExpiredStrategy())         // 만료된 세션으로 요청 시, 처리
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

        @Bean
        public SessionRegistry sessionRegistry() {
            return new SessionRegistryImpl();
        }

        @Bean
        public CustomSessionExpiredStrategy customSessionExpiredStrategy() {
            return new CustomSessionExpiredStrategy();
        }

        // 세션 생성, 만료 이벤트 리스너
        @Bean
        public static ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
            return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
        }
}