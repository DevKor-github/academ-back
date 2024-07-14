package com.example.Devkor_project.security;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.ResponseDto;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.repository.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class AccessAuthFilter extends OncePerRequestFilter {

    private final ProfileRepository profileRepository;
    private final VersionProvider versionProvider;
    private final ObjectMapper objectMapper;

    // 강의평 열람 권한 확인 필터
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("access filter!!!");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((CustomUserDetails) principal).getUsername();

        if (email != null) {
            Optional<Profile> profile = profileRepository.findByEmail(email);

            if (profile.isPresent() && profile.get().getAccess_expiration_date().isAfter(LocalDate.now())) {
                // 다음 필터로 전달
                filterChain.doFilter(request, response);
                return;
            }
        }

        ResponseDto.Error dto = ResponseDto.Error.builder()
                .code("NO_ACCESS_AUTHORITY")
                .message("강의평 접근 권한이 만료되었습니다.")
                .data(null)
                .version(versionProvider.getVersion())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(dto));
        response.getWriter().flush();
    }

    // 필터를 적용하지 않을 경로 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {
                "/api/login", "/api/logout", "/api/signup",
                "/api/check-login", "/api/refresh-token",
                "/api/point",
        };
        String path = request.getRequestURI();

        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

}
