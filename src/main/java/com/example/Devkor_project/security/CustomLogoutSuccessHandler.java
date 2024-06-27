package com.example.Devkor_project.security;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.ResponseDto;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler
{
    @Autowired JwtUtil jwtUtil;
    @Autowired RedisTemplate<String, String> redisTemplate;
    @Autowired ProfileRepository profileRepository;
    @Autowired VersionProvider versionProvider;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException
    {
        String authorizationHeader = request.getHeader("Authorization");

        // 헤더에 JWT token이 존재하는지 체크
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
        {
            String token = authorizationHeader.substring(7);

            // JWT 유효성 검증
            if(jwtUtil.validateToken(token))
            {
                Long userId = jwtUtil.getUserId(token);

                Profile profile = profileRepository.findById(userId)
                        .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, null));

                String email = profile.getEmail();

                // redis에서 token 정보 삭제
                if (redisTemplate.opsForValue().get(email) != null) {
                    redisTemplate.delete(email);
                }
            }
        }

        ResponseDto.Success dto = ResponseDto.Success.builder()
                .data(null)
                .message("로그아웃을 성공하였습니다.")
                .version(versionProvider.getVersion())
                .build();

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(dto));
        response.getWriter().flush();
    }
}
