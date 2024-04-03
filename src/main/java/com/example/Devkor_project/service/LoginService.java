package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.LoginRequestDto;
import com.example.Devkor_project.dto.SignUpRequestDto;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.ProfileRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.apache.catalina.filters.ExpiresFilter;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService
{
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    /*
        < 로그인 Service >
        이메일이 해당하는 계정이 존재하지 않을 경우,
        비밀번호가 일치하지 않을 경우에 대해서 예외를 발생시킵니다.
    */
    @Transactional
    public void login(LoginRequestDto dto)
    {
        // 이메일에 해당하는 계정 존재 여부 체크
        Profile profile = profileRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 일치하지 않습니다."));

        // 비밀번호 체크
        if(!encoder.matches(dto.getPassword(), profile.getPassword()))
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 일치하지 않습니다.");

    }

    /*
        < 회원가입 Service >
        SignUpRequestDto를 받아서
        해당 이메일이 사용 중일 경우, 예외를 발생시키고,
        해당 이메일이 사용 중이지 않으면, 데이터베이스에 프로필 정보를 저장합니다.
    */
    @Transactional
    public void signUp(SignUpRequestDto dto)
    {
        // 이메일 중복 체크
        profileRepository.findByEmail(dto.getEmail())
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.EMAIL_DUPLICATED, dto.getEmail() + "는 이미 사용 중입니다.");
                });

        // DTO -> Entity 변환
        Profile profile = Profile.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .studentId(dto.getStudentId())
                .grade(dto.getGrade())
                .semester(dto.getSemester())
                .department(dto.getDepartment())
                .build();

        // 해당 Entity를 데이터베이스에 저장
        profileRepository.save(profile);
    }
}
