package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.SignUpRequestDto;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.ProfileRepository;
import jakarta.transaction.Transactional;
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
        < 회원가입 Service >
        SignUpRequestDto를 받아서
        해당 이메일이 사용 중일 경우, 에러를 발생시키고,
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
