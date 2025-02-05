package com.example.Devkor_project.security;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.repository.ProfileRepository;
import com.example.Devkor_project.repository.TrafficRepository;
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
        @Autowired TrafficRepository trafficRepository;
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
                                // 로그인
                                .requestMatchers("/api/signup").permitAll()
                                .requestMatchers("/api/login").permitAll()
                                .requestMatchers("/api/logout").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/signup/send-email").permitAll()
                                .requestMatchers("/api/signup/check-email").permitAll()
                                .requestMatchers("/api/signup/check-username").permitAll()
                                .requestMatchers("/api/login/reset-password").permitAll()
                                .requestMatchers("/api/check-login").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/refresh-token").permitAll()
                                // ADMIN
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                // 강의
                                .requestMatchers("/api/course/search").permitAll()
                                .requestMatchers("/api/course/search/count-result").permitAll()
                                .requestMatchers("/api/course/detail").permitAll()
                                .requestMatchers("/api/course/bookmark").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/course/start-insert-comment").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/course/insert-comment").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/course/start-update-comment").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/course/update-comment").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/course/delete-comment").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/course/like-comment").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/course/report-comment").hasAnyRole("USER", "ADMIN")
                                // 마이페이지
                                .requestMatchers("/api/mypage/**").hasAnyRole("USER", "ADMIN")
                                //시간표, 개인일정
                                .requestMatchers("/api/privacy/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/timetables/**").hasAnyRole("USER", "ADMIN")
                                // 기타
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/api/is-secure").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                // 그 외의 요청은 로그인을 수행한 사용자에게 접근 권한 허용
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

                // JwtAuthFilter 앞에 TrafficFilter 추가
                httpSecurity
                        .addFilterBefore(new TrafficFilter(trafficRepository), JwtAuthFilter.class);

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