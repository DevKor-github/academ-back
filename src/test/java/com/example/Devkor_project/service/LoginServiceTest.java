package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.ProfileDto;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.CodeRepository;
import com.example.Devkor_project.repository.ProfileRepository;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class LoginServiceTest
{
    @Autowired(required = false) LoginService loginService;

    @Autowired ProfileRepository profileRepository;
    @Autowired CodeRepository codeRepository;

    @MockBean JavaMailSender javaMailSender;

    private String email;
    private String purpose;
    private ProfileDto.Signup profileDto;
    private MimeMessage mimeMessage;

    /* 테스트 별 초기 설정 */
    @BeforeEach
    void conditionalSetUp(TestInfo testInfo)
    {
        if (testInfo.getDisplayName().startsWith("회원가입"))
        {
            email = "test1234@korea.ac.kr";
            purpose = "SIGN_UP";
            profileDto = ProfileDto.Signup.builder()
                    .email("test1234@korea.ac.kr")
                    .password("test1234")
                    .username("test1234")
                    .student_id("1234567")
                    .degree("MASTER")
                    .semester(1)
                    .department("test")
                    .build();
            mimeMessage = mock(MimeMessage.class);

        }
    }

    @Test
    @DisplayName("회원가입 프로세스 성공")
    void join_process_success()
    {
        // Given
        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        // When
        loginService.sendAuthenticationNumber(email, purpose);

        String code = codeRepository.findByEmail(email).get().getCode();
        loginService.checkAuthenticationNumber(email, code);

        profileDto.setCode(code);
        loginService.signUp(profileDto);

        // Then
        verify(javaMailSender, times(1)).send(mimeMessage);
        assertThat(profileRepository.findByEmail(email).isPresent()).isTrue();
    }

    @Test
    @DisplayName("회원가입 프로세스 실패 1 : 이메일 인증번호 전송 시, 고려대 이메일이 아닌 경우")
    void join_process_failure_1()
    {
        // Given
        email = "test1234@naver.com";

        // When & Then
        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.sendAuthenticationNumber(email, purpose)
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.EMAIL_NOT_KOREA);
    }

    @Test
    @DisplayName("회원가입 프로세스 실패 2 : purpose 형식이 잘못된 경우")
    void join_process_failure_2()
    {
        // Given
        purpose = "wrong_purpose";

        // When & Then
        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.sendAuthenticationNumber(email, purpose)
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_PURPOSE);
    }

    @Test
    @DisplayName("회원가입 프로세스 실패 3 : 이메일 인증번호 확인 시, 고려대 이메일이 아닌 경우")
    void join_process_failure_3()
    {
        // Given
        email = "test1234@naver.com";
        String code = "12345678";

        // When & Then
        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.checkAuthenticationNumber(email, code)
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.EMAIL_NOT_KOREA);
    }

    @Test
    @DisplayName("회원가입 프로세스 실패 4 : 이메일 인증번호 확인 시, 해당 이메일로 발송된 인증번호가 없는 경우")
    void join_process_failure_4()
    {
        // Given
        String code = "12345678";

        // When & Then
        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.checkAuthenticationNumber(email, code)
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CODE_NOT_FOUND);
    }

    @Test
    @DisplayName("회원가입 프로세스 실패 5 : 이메일 인증번호 확인 시, 입력한 인증번호가 틀린 경우")
    void join_process_failure_5()
    {
        // Given
        String code = "12345678";

        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        // When & Then
        loginService.sendAuthenticationNumber(email, purpose);
        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.checkAuthenticationNumber(email, code)
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.WRONG_CODE);
    }

    @Test
    @DisplayName("회원가입 프로세스 실패 6 : 이메일 인증번호 확인 시, 이메일이 중복된 경우")
    void join_process_failure_6()
    {
        // Given
        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        // When & Then
        loginService.sendAuthenticationNumber(email, purpose);

        Profile profile = Profile.builder()
                .email("test1234@korea.ac.kr")
                .password("test1234")
                .username("test1234")
                .student_id("1234567")
                .degree("MASTER")
                .semester(1)
                .department("test")
                .point(0)
                .access_expiration_date(LocalDate.now())
                .created_at(LocalDate.now())
                .role("ROLE_USER")
                .build();
        profileRepository.save(profile);

        String code = codeRepository.findByEmail(email).get().getCode();

        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.checkAuthenticationNumber(email, code)
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.EMAIL_DUPLICATED);
    }

    @Test
    @DisplayName("회원가입 프로세스 실패 7 : 회원가입 시, 해당 이메일로 발송된 인증번호가 없는 경우")
    void join_process_failure_7()
    {
        // When & Then
        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.signUp(profileDto)
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CODE_NOT_FOUND);
    }

    @Test
    @DisplayName("회원가입 프로세스 실패 8 : 회원가입 시, 입력한 인증번호가 틀린 경우")
    void join_process_failure_8()
    {
        // Given
        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        // When & Then
        loginService.sendAuthenticationNumber(email, purpose);

        String code = codeRepository.findByEmail(email).get().getCode();
        loginService.checkAuthenticationNumber(email, code);

        profileDto.setCode("wrong_code");
        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.signUp(profileDto)
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.WRONG_CODE);
    }

    @Test
    @DisplayName("회원가입 프로세스 실패 9 : 회원가입 시, 이메일이 중복된 경우")
    void join_process_failure_9()
    {
        // Given
        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        // When & Then
        loginService.sendAuthenticationNumber(email, purpose);

        String code = codeRepository.findByEmail(email).get().getCode();
        loginService.checkAuthenticationNumber(email, code);

        Profile profile = Profile.builder()
                .email("test1234@korea.ac.kr")
                .password("test1234")
                .username("test1234")
                .student_id("1234567")
                .degree("MASTER")
                .semester(1)
                .department("test")
                .point(0)
                .access_expiration_date(LocalDate.now())
                .created_at(LocalDate.now())
                .role("ROLE_USER")
                .build();
        profileRepository.save(profile);

        profileDto.setCode(code);
        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.signUp(profileDto)
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.EMAIL_DUPLICATED);
    }

    @Test
    @DisplayName("회원가입 프로세스 실패 10 : 회원가입 시, 학번 형식 오류")
    void join_process_failure_10()
    {
        // Given
        profileDto.setStudent_id("1234567890");

        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        // When & Then
        loginService.sendAuthenticationNumber(email, purpose);

        String code = codeRepository.findByEmail(email).get().getCode();
        loginService.checkAuthenticationNumber(email, code);

        profileDto.setCode(code);
        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.signUp(profileDto)
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_STUDENT_ID);
    }

    @ParameterizedTest(name = "회원가입 시, 비밀번호 형식 오류 {index} : password = {0}")
    @CsvSource({
            "test1",
            "test123456789012345678901234567890",
            "testtesttest",
            "12345678901234567890"
    })
    @DisplayName("회원가입 프로세스 실패 11 : 회원가입 시, 비밀번호 형식 오류")
    void join_process_failure_11(String password)
    {
        // Given
        profileDto.setPassword(password);

        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        // When & Then
        loginService.sendAuthenticationNumber(email, purpose);

        String code = codeRepository.findByEmail(email).get().getCode();
        loginService.checkAuthenticationNumber(email, code);

        profileDto.setCode(code);
        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.signUp(profileDto)
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_PASSWORD);
    }

    @Test
    @DisplayName("회원가입 프로세스 실패 12 : 회원가입 시, 닉네임 형식 오류")
    void join_process_failure_12()
    {
        // Given
        profileDto.setUsername("12345678901234567890");

        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        // When & Then
        loginService.sendAuthenticationNumber(email, purpose);

        String code = codeRepository.findByEmail(email).get().getCode();
        loginService.checkAuthenticationNumber(email, code);

        profileDto.setCode(code);
        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.signUp(profileDto)
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_USERNAME);
    }

    @Test
    @DisplayName("회원가입 프로세스 실패 13 : 회원가입 시, 닉네임이 중복된 경우")
    void join_process_failure_13()
    {
        // Given
        Profile profile = Profile.builder()
                .email("another@korea.ac.kr")
                .password("test1234")
                .username("test1234")
                .student_id("1234567")
                .degree("MASTER")
                .semester(1)
                .department("test")
                .point(0)
                .access_expiration_date(LocalDate.now())
                .created_at(LocalDate.now())
                .role("ROLE_USER")
                .build();
        profileRepository.save(profile);

        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        // When & Then
        loginService.sendAuthenticationNumber(email, purpose);

        String code = codeRepository.findByEmail(email).get().getCode();
        loginService.checkAuthenticationNumber(email, code);

        profileDto.setCode(code);
        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.signUp(profileDto)
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USERNAME_DUPLICATED);
    }

    @Test
    @DisplayName("회원가입 프로세스 실패 14 : 회원가입 시, 학위 형식 오류")
    void join_process_failure_14()
    {
        // Given
        profileDto.setDegree("wrong_degree");

        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        // When & Then
        loginService.sendAuthenticationNumber(email, purpose);

        String code = codeRepository.findByEmail(email).get().getCode();
        loginService.checkAuthenticationNumber(email, code);

        profileDto.setCode(code);
        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.signUp(profileDto)
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_DEGREE);
    }
}