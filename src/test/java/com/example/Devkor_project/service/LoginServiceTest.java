package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.SignUpRequestDto;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Nested
@AutoConfigureMockMvc
class LoginServiceTest {

    // Arrange
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    LoginService loginService;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    SignUpRequestDto signUpRequestDto = new SignUpRequestDto(
            "test@gmail.com",
            "test",
            "test",
            "0000000000",
            1,
            1,
            "test"
    );

    LoginRequestDto loginRequestDto = new LoginRequestDto(
            "test@gmail.com",
            "test",
            true
    );

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTest {
        @Test
        @Transactional
        @DisplayName("로그인 성공 테스트")
        void loginTest_success()
        {
            // Arrange
            profileRepository.save(Profile.builder()
                    .email(signUpRequestDto.getEmail())
                    .password(encoder.encode(signUpRequestDto.getPassword()))
                    .username(signUpRequestDto.getUsername())
                    .studentId(signUpRequestDto.getStudentId())
                    .grade(signUpRequestDto.getGrade())
                    .semester(signUpRequestDto.getSemester())
                    .department(signUpRequestDto.getDepartment())
                    .build()
            );
            boolean isError = false;

            // Act
            try {
                loginService.login(loginRequestDto);
            }
            catch(AppException e) {
                isError = true;
            }

            // Assert
            assertFalse(isError);
        }

        @Test
        @Transactional
        @DisplayName("로그인 실패 테스트 (존재하지 않는 계정)")
        void loginTest_failure_nonExistAccount()
        {
            // Arrange
            boolean isError = false;

            // Act
            try {
                loginService.login(loginRequestDto);
            }
            catch(AppException e) {
                isError = true;
            }

            // Assert
            assertTrue(isError);
        }

        @Test
        @Transactional
        @DisplayName("로그인 실패 테스트 (비밀번호 불일치)")
        void loginTest_failure_wrongPassword()
        {
            // Arrange
            profileRepository.save(Profile.builder()
                    .email(signUpRequestDto.getEmail())
                    .password(encoder.encode(signUpRequestDto.getPassword()))
                    .username(signUpRequestDto.getUsername())
                    .studentId(signUpRequestDto.getStudentId())
                    .grade(signUpRequestDto.getGrade())
                    .semester(signUpRequestDto.getSemester())
                    .department(signUpRequestDto.getDepartment())
                    .build()
            );
            LoginRequestDto wrongLoginRequestDto = new LoginRequestDto(
                    "test@gmail.com",
                    "testttttttttttttt",
                    true
            );
            boolean isError = false;

            // Act
            try {
                loginService.login(wrongLoginRequestDto);
            }
            catch(AppException e) {
                isError = true;
            }

            // Assert
            assertTrue(isError);
        }
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class SignUpTest {
        @Test
        @Transactional
        @DisplayName("회원가입 성공 테스트")
        void signUpTest_success()
        {
            // Arrange
            loginService.signUp(signUpRequestDto);

            // Act
            String result = profileRepository.findByEmail("test@gmail.com").orElse(null).getEmail();

            // Assert
            assertEquals(
                    "test@gmail.com",
                    result
            );
        }

        @Test
        @Transactional
        @DisplayName("회원가입 실패 테스트 (중복된 요청)")
        void signUpTest_failure()
        {
            // Arrange
            Profile profile = signUpRequestDto.toEntity();
            profileRepository.save(profile);

            // Act
            ResponseEntity<String> result = null;
            try {
                loginService.signUp(signUpRequestDto);
            }
            catch (AppException e) {
                result = ResponseEntity.status(e.getErrorCode().getHttpStatus())
                        .body(e.getErrorCode() + ": " + e.getMessage());;
            }

            // Assert
            assertEquals(
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body("EMAIL_DUPLICATED: test@gmail.com는 이미 사용 중입니다."),
                    result
            );
        }
    }

}