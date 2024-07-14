package com.example.Devkor_project.security;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.repository.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
        @Autowired JwtUtil jwtUtil;
        @Autowired RedisTemplate<String, String> redisTemplate;
        @Autowired ProfileRepository profileRepository;
        @Autowired VersionProvider versionProvider;
        @Autowired ObjectMapper objectMapper = new ObjectMapper();

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                       CustomUserDetailsService customUserDetailsService) throws Exception {
                // 기본 설정
                httpSecurity
                        .httpBasic(HttpBasicConfigurer::disable)    // HTTP 기본 인증 비활성화
                        .csrf(CsrfConfigurer::disable);             // CSRF 보호 비활성화

                // 경로별 권한 설정
                httpSecurity
                        .authorizeHttpRequests((requests) -> (requests)
                                // 해당 요청은 모든 사용자에게 접근 권한 허용
                                .requestMatchers("/", "/login", "/signup").permitAll()
                                .requestMatchers("/api/login/**", "/api/signup/**").permitAll()
                                .requestMatchers("/api/refresh-token").permitAll()
                                // 해당 요청은 인증된 사용자에게만 접근 권한 허용
                                .requestMatchers("/api/logout").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/check-login").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/course/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/course/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/point/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/point/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/mypage/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/mypage/**").hasAnyRole("USER", "ADMIN")
                                // 해당 요청은 관리자에게만 접근 권한 허용
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                // 그 외의 요청은 모든 사용자에게 접근 권한 허용
                                .anyRequest().authenticated()
                        );

                // 예외 처리 설정
                httpSecurity
                        .exceptionHandling((exception) -> exception
                                .authenticationEntryPoint(customAuthenticationEntryPoint())
                                .accessDeniedHandler(customAccessDeniedHandler())
                        );

                // UsernamePasswordAuthenticationFilter 앞에 AccessAuthFilter 추가
                httpSecurity
                        .addFilterBefore(new AccessAuthFilter(profileRepository, versionProvider, objectMapper), UsernamePasswordAuthenticationFilter.class);

                // AccessAuthFilter 앞에 JwtAuthFilter 추가
                httpSecurity
                        .addFilterBefore(new JwtAuthFilter(customUserDetailsService, jwtUtil, redisTemplate, profileRepository), AccessAuthFilter.class);

                // 로그인, 로그아웃 설정
                httpSecurity
                        .formLogin((auth) -> auth
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .loginProcessingUrl("/api/login")
                                .permitAll()
                                .successHandler(customAuthSuccessHandler())
                                .failureHandler(customAuthFailureHandler())
                        )
                        .logout((logoutConfig) -> logoutConfig
                                .logoutUrl("/api/logout")
                                .logoutSuccessHandler(customLogoutSuccessHandler())
                        )
                        .userDetailsService(customUserDetailsService);

                // 세션 생성 및 사용 정지
                httpSecurity
                        .sessionManagement((session) -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        );

                return httpSecurity.build();
        }

        @Bean
        public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
                return new CustomAuthenticationEntryPoint();
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

        // 세션 생성, 만료 이벤트 리스너
        @Bean
        public static ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
                return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
        }
}