package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.ProfileDto;
import com.example.Devkor_project.entity.Code;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.CodeRepository;
import com.example.Devkor_project.repository.ProfileRepository;
import com.example.Devkor_project.security.CustomUserDetailsService;
import com.example.Devkor_project.security.JwtUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest
{
    @InjectMocks
    private LoginService loginService;

    @Mock private ProfileRepository profileRepository;
    @Mock private CodeRepository codeRepository;
    @Spy private BCryptPasswordEncoder encoder;
    @Mock private JavaMailSender javaMailSender;
    @Mock private JwtUtil jwtUtil;
    @Mock private CustomUserDetailsService customUserDetailService;
    @Mock private RedisTemplate<String, String> redisTemplate;

    private ProfileDto.Signup profileDto;
    private Code code;

    /* 테스트 별 초기 설정 */
    @BeforeEach
    void conditionalSetUp(TestInfo testInfo)
    {
        if (
                testInfo.getDisplayName().startsWith("회원가입 성공") ||
                testInfo.getDisplayName().startsWith("회원가입 실패")
        )
        {
            profileDto = ProfileDto.Signup.builder()
                    .email("test1234@korea.ac.kr")
                    .password("test1234")
                    .username("test1234")
                    .student_id("1234567")
                    .degree("MASTER")
                    .semester(1)
                    .department("test")
                    .code("12345678")
                    .build();

            code = Code.builder()
                    .code_id(1L)
                    .email("test1234@korea.ac.kr")
                    .code("12345678")
                    .created_at(LocalDate.now())
                    .build();
        }
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success()
    {
        // Given
        given(codeRepository.findByEmail(profileDto.getEmail()))
                .willReturn(Optional.of(code));
        given(profileRepository.findByEmail(profileDto.getEmail()))
                .willReturn(Optional.empty());
        given(profileRepository.findByUsername(profileDto.getUsername()))
                .willReturn(Optional.empty());

        // When
        loginService.signUp(profileDto);

        // Then
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    @DisplayName("회원가입 실패 1: 해당 이메일로 발송된 인증번호가 존재하지 않음")
    void signUp_fail_1()
    {
        // Given
        given(codeRepository.findByEmail(profileDto.getEmail()))
                .willReturn(Optional.empty());

        // When & Then
        AppException exception = assertThrows(AppException.class, () -> loginService.signUp(profileDto));
        assertEquals(ErrorCode.CODE_NOT_FOUND, exception.getErrorCode());

        verify(profileRepository, times(0)).save(any(Profile.class));
    }

    @Test
    @DisplayName("회원가입 실패 2: 인증번호가 틀림")
    void signUp_fail_2()
    {
        // Given
        profileDto.setCode("wrong_code");

        given(codeRepository.findByEmail(profileDto.getEmail()))
                .willReturn(Optional.of(code));

        // When & Then
        AppException exception = assertThrows(AppException.class, () -> loginService.signUp(profileDto));
        assertEquals(ErrorCode.WRONG_CODE, exception.getErrorCode());

        verify(profileRepository, times(0)).save(any(Profile.class));
    }

    @Test
    @DisplayName("회원가입 실패 3: 이메일 중복")
    void signUp_fail_3()
    {
        // Given
        given(codeRepository.findByEmail(profileDto.getEmail()))
                .willReturn(Optional.of(code));
        given(profileRepository.findByEmail(profileDto.getEmail()))
                .willReturn(Optional.of(new Profile()));

        // When & Then
        AppException exception = assertThrows(AppException.class, () -> loginService.signUp(profileDto));
        assertEquals(ErrorCode.EMAIL_DUPLICATED, exception.getErrorCode());

        verify(profileRepository, times(0)).save(any(Profile.class));
    }

    @Test
    @DisplayName("회원가입 실패 4: 학번 형식 오류")
    void signUp_fail_4()
    {
        // Given
        profileDto.setStudent_id("12345678");

        given(codeRepository.findByEmail(profileDto.getEmail()))
                .willReturn(Optional.of(code));
        given(profileRepository.findByEmail(profileDto.getEmail()))
                .willReturn(Optional.empty());

        // When & Then
        AppException exception = assertThrows(AppException.class, () -> loginService.signUp(profileDto));
        assertEquals(ErrorCode.INVALID_STUDENT_ID, exception.getErrorCode());

        verify(profileRepository, times(0)).save(any(Profile.class));
    }

    @ParameterizedTest(name = "회원가입 실패 5: 비밀번호가 {0}")
    @CsvSource({
            "test",
            "test012345678901234567890123456789",
            "0123456789",
            "abcdefghij"
    })
    @DisplayName("회원가입 실패 5: 비밀번호 형식 오류")
    void signUp_fail_5(String password)
    {
        // Given
        profileDto.setPassword(password);

        given(codeRepository.findByEmail(profileDto.getEmail()))
                .willReturn(Optional.of(code));
        given(profileRepository.findByEmail(profileDto.getEmail()))
                .willReturn(Optional.empty());

        // When & Then
        AppException exception = assertThrows(AppException.class, () -> loginService.signUp(profileDto));
        assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());

        verify(profileRepository, times(0)).save(any(Profile.class));
    }

    @Test
    @DisplayName("회원가입 실패 6: 닉네임 형식 오류")
    void signUp_fail_6()
    {
        // Given
        profileDto.setUsername("01234567890123456789");

        given(codeRepository.findByEmail(profileDto.getEmail()))
                .willReturn(Optional.of(code));
        given(profileRepository.findByEmail(profileDto.getEmail()))
                .willReturn(Optional.empty());

        // When & Then
        AppException exception = assertThrows(AppException.class, () -> loginService.signUp(profileDto));
        assertEquals(ErrorCode.INVALID_USERNAME, exception.getErrorCode());

        verify(profileRepository, times(0)).save(any(Profile.class));
    }

    @Test
    @DisplayName("회원가입 실패 7: 닉네임 중복")
    void signUp_fail_7()
    {
        // Given
        given(codeRepository.findByEmail(profileDto.getEmail()))
                .willReturn(Optional.of(code));
        given(profileRepository.findByEmail(profileDto.getEmail()))
                .willReturn(Optional.empty());
        given(profileRepository.findByUsername(profileDto.getUsername()))
                .willReturn(Optional.of(new Profile()));

        // When & Then
        AppException exception = assertThrows(AppException.class, () -> loginService.signUp(profileDto));
        assertEquals(ErrorCode.USERNAME_DUPLICATED, exception.getErrorCode());

        verify(profileRepository, times(0)).save(any(Profile.class));
    }

    @Test
    @DisplayName("회원가입 실패 8: 학위 형식 오류")
    void signUp_fail_8()
    {
        // Given
        profileDto.setDegree("wrong_degree");

        given(codeRepository.findByEmail(profileDto.getEmail()))
                .willReturn(Optional.of(code));
        given(profileRepository.findByEmail(profileDto.getEmail()))
                .willReturn(Optional.empty());
        given(profileRepository.findByUsername(profileDto.getUsername()))
                .willReturn(Optional.empty());

        // When & Then
        AppException exception = assertThrows(AppException.class, () -> loginService.signUp(profileDto));
        assertEquals(ErrorCode.INVALID_DEGREE, exception.getErrorCode());

        verify(profileRepository, times(0)).save(any(Profile.class));
    }

    @Test
    void sendAuthenticationNumber() {
    }

    @Test
    void checkAuthenticationNumber() {
    }

    @Test
    void checkUsername() {
    }

    @Test
    void resetPassword() {
    }

    @Test
    void checkLogin() {
    }

    @Test
    void refreshToken() {
    }
}