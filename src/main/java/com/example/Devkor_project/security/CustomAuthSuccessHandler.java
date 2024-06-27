package com.example.Devkor_project.security;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.ProfileDto;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler
{
    @Autowired JwtUtil jwtUtil;
    @Autowired RedisTemplate<String, String> redisTemplate;
    @Autowired ProfileRepository profileRepository;
    @Autowired VersionProvider versionProvider;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException
    {
        String email = authentication.getName();

        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.LOGIN_FAILURE, email));

        ProfileDto.Profile profileDto = ProfileDto.Profile.builder()
                .profile_id(profile.getProfile_id())
                .email(profile.getEmail())
                .password(profile.getPassword())
                .username(profile.getUsername())
                .student_id(profile.getStudent_id())
                .degree(profile.getDegree())
                .semester(profile.getSemester())
                .department(profile.getDepartment())
                .point(profile.getPoint())
                .created_at(profile.getCreated_at())
                .role(profile.getRole())
                .build();

        // access token 발행
        String accessToken = jwtUtil.createAccessToken(profileDto);

        // redis에 token 정보 저장
        redisTemplate.opsForValue().set(email, accessToken);

        ResponseDto.Success dto = ResponseDto.Success.builder()
                .data(accessToken)
                .message("로그인을 성공하였습니다.")
                .version(versionProvider.getVersion())
                .build();

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(dto));
        response.getWriter().flush();
    }
}
