package com.example.Academ.controller;

import com.example.Academ.dto.login.SignUpRequestDto;
import com.example.Academ.entity.Profile;
import com.example.Academ.exception.AppException;
import com.example.Academ.exception.ErrorCode;
import com.example.Academ.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginControllerTest {

    @Autowired
    LoginController loginController;

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
        // 예상
        ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.CREATED).body("회원가입을 성공하였습니다.");

        // 실제
        ResponseEntity<String> actualResult = loginController.signUp(testDto);

        // 비교하여 검증
        assertEquals(expectedResult, actualResult);
    }

    // 회원가입 실패 시뮬레이션 테스트 (이메일 중복)
    @Test
    @Transactional
    void signUp_failure()
    {
        // 테스트용 DTO를 Entity로 변환 후, 데이터베이스에 저장
        Profile profile = testDto.toEntity();
        profileRepository.save(profile);

        // 예상
        ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("EMAIL_DUPLICATED: test@gmail.com는 이미 사용 중입니다.");

        // 실제
        ResponseEntity<String> actualResult;
        try
        {
            actualResult = loginController.signUp(testDto);
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