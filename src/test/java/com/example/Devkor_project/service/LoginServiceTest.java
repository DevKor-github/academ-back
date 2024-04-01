package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.SignUpRequestDto;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginServiceTest {

    @Autowired
    LoginService loginService;

    @Autowired
    ProfileRepository profileRepository;

    // 테스트용 DTO
    SignUpRequestDto testDto = new SignUpRequestDto(
            "test@gmail.com",
            "test",
            "test",
            "0000000000",
            1,
            1,
            "test"
    );

    // 회원가입 성공 시뮬레이션 테스트
    @Test
    @Transactional
    void signUp_success()
    {
        // 회원가입 Service 처리
        loginService.signUp(testDto);

        // 예상
        String expectedResult = "test@gmail.com";

        // 실제
        String actualResult = profileRepository.findByEmail("test@gmail.com").orElse(null).getEmail();

        // 비교하여 검증
        assertEquals(expectedResult, actualResult);
    }

    // 회원가입 실패 시뮬레이션 테스트 (이메일 중복)
    @Test
    @Transactional
    void signUp_failure()
    {
        // 테스트용 Entity 데이터베이스에 저장
        Profile profile = testDto.toEntity();
        profileRepository.save(profile);

        // 예상
        ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("EMAIL_DUPLICATED: test@gmail.com는 이미 사용 중입니다.");

        // 실제
        ResponseEntity<String> actualResult = null;
        try
        {
            loginService.signUp(testDto);
        }
        catch (AppException e)
        {
            actualResult = ResponseEntity.status(e.getErrorCode().getHttpStatus())
                    .body(e.getErrorCode() + ": " + e.getMessage());;
        }

        // 비교하여 검증
        assertEquals(expectedResult, actualResult);
    }
}