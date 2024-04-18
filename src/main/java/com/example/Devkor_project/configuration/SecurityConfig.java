package com.example.Devkor_project.configuration;

import com.example.Devkor_project.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CustomUserDetailsService customUserDetailsService) throws Exception
    {
        httpSecurity
                .httpBasic(HttpBasicConfigurer::disable)    // HTTP 기본 인증 비활성화
                .csrf(CsrfConfigurer::disable)              // CSRF 보호 비활성화
                .cors(Customizer.withDefaults());           // CORS를 기본 값으로 활성화

        httpSecurity
                .authorizeHttpRequests((requests) -> (requests)
                        // 해당 요청은 모든 사용자에게 접근 권한 허용
                        .requestMatchers("/", "/login", "/signup").permitAll()
                        .requestMatchers("/api/login/**", "/api/signup/**").permitAll()
                        .requestMatchers("/mypage").hasAnyRole("USER", "ADMIN") // 예시
                        .requestMatchers("/api/admin/**", "/admin").hasRole("ADMIN") // Admin 만 접근 가능
                        // 그 외의 요청은 인증된 사용자에게만 접근 권한 허용
                        .anyRequest().authenticated()
                );

        httpSecurity
                .formLogin((auth) -> auth
                        .usernameParameter("email") // email 변경 예정
                        .passwordParameter("password")
                        .loginPage("/login")  // frontend login page
                        .loginProcessingUrl("/api/login")   // post api
                        .permitAll()
                        .defaultSuccessUrl("/", true)   // success 시 direct
                        .failureHandler(customAuthFailureHandler())
                );

        httpSecurity
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID", "remember-me");

        httpSecurity
                .rememberMe()
                .key("Test-Key-For-Academ")
                .tokenValiditySeconds(60 * 60 * 24 * 30) // 30일
                .rememberMeParameter("remember-me")
                .userDetailsService(customUserDetailsService);

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
    public AuthenticationFailureHandler customAuthFailureHandler() {
        return new CustomAuthFailureHandler();
    }
}
