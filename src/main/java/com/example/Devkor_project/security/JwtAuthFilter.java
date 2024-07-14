package com.example.Devkor_project.security;

import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.ProfileRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter
{
    private final CustomUserDetailsService customUserDetailService;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final ProfileRepository profileRepository;

    // JWT 토큰 검증 필터
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("auth filter!!!");

        String authorizationHeader = request.getHeader("Authorization");

        // 헤더에 JWT token이 존재하는지 체크
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
        {
            String token = authorizationHeader.substring(7);

            // JWT 유효성 검증
            if(jwtUtil.validateToken(token))
            {
                Long userId = jwtUtil.getProfileId(token);
                UserDetails userDetails = customUserDetailService.loadUserByProfileId(userId);

                Profile profile = profileRepository.findById(userId)
                        .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, null));

                String email = profile.getEmail();

                if(userDetails != null && Boolean.TRUE.equals(redisTemplate.hasKey(email)) && Objects.equals(redisTemplate.opsForValue().get(email), token))
                {
                    // 접근 권한 인증 token 생성
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // 현재 요청의 security context에 접근 권한 부여
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        // 다음 필터로 전달
        filterChain.doFilter(request, response);
    }
}
