package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.ProfileDto;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.CodeRepository;
import com.example.Devkor_project.repository.ProfileRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LoginServiceTest
{
    @Autowired MockMvc mockMvc;
    @Autowired(required = false) LoginService loginService;

    @Autowired ProfileRepository profileRepository;
    @Autowired CodeRepository codeRepository;

    @MockBean JavaMailSender javaMailSender;

    private String email;
    private ProfileDto.Signup profileDto;
    private MimeMessage mimeMessage;

    /* 테스트 별 초기 설정 */
    @BeforeEach
    void conditionalSetUp(TestInfo testInfo)
    {
        email = "test1234@korea.ac.kr";
        profileDto = ProfileDto.Signup.builder()
                .email(email)
                .password("test1234")
                .username("test1234")
                .student_id("1234567")
                .degree("MASTER")
                .semester(1)
                .department("test")
                .build();
        mimeMessage = mock(MimeMessage.class);
    }

    // profileDto에 해당하는 계정 생성
    void createAccount(String email)
    {
        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        loginService.sendAuthenticationNumber(email, "SIGN_UP");

        String code = codeRepository.findByEmail(email).get().getCode();
        loginService.checkAuthenticationNumber(email, code);

        profileDto.setEmail(email);
        profileDto.setCode(code);
        loginService.signUp(profileDto);
    }

    @Test
    @DisplayName("회원가입 프로세스 성공")
    void join_process_success() throws Exception
    {
        // Given
        createAccount(email);

        // Then
        verify(javaMailSender, times(1)).send(mimeMessage);
        assertThat(profileRepository.findByEmail(email).isPresent()).isTrue();

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", profileDto.getEmail())
                        .param("password", profileDto.getPassword())
                        .param("remember-me", "true"))
                .andExpect(status().isOk());
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
                () -> loginService.sendAuthenticationNumber(email, "SIGN_UP")
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.EMAIL_NOT_KOREA);
    }

    @Test
    @DisplayName("회원가입 프로세스 실패 2 : purpose 형식이 잘못된 경우")
    void join_process_failure_2()
    {
        // When & Then
        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.sendAuthenticationNumber(email, "wrong_purpose")
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
        loginService.sendAuthenticationNumber(email, "SIGN_UP");
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
        loginService.sendAuthenticationNumber(email, "SIGN_UP");

        createAccount(email);

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
        loginService.sendAuthenticationNumber(email, "SIGN_UP");

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
        loginService.sendAuthenticationNumber(email, "SIGN_UP");

        String code = codeRepository.findByEmail(email).get().getCode();
        loginService.checkAuthenticationNumber(email, code);

        createAccount(email);
        code = codeRepository.findByEmail(email).get().getCode();

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
        loginService.sendAuthenticationNumber(email, "SIGN_UP");

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
        loginService.sendAuthenticationNumber(email, "SIGN_UP");

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
        loginService.sendAuthenticationNumber(email, "SIGN_UP");

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
        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        profileDto.setEmail("another_email@korea.ac.kr");
        createAccount("another_email@korea.ac.kr");

        // When & Then
        loginService.sendAuthenticationNumber(email, "SIGN_UP");

        String code = codeRepository.findByEmail(email).get().getCode();
        loginService.checkAuthenticationNumber(email, code);

        profileDto.setEmail(email);
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
        loginService.sendAuthenticationNumber(email, "SIGN_UP");

        String code = codeRepository.findByEmail(email).get().getCode();
        loginService.checkAuthenticationNumber(email, code);

        profileDto.setCode(code);
        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.signUp(profileDto)
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_DEGREE);
    }

    @Test
    @DisplayName("임시 비밀번호 발급 프로세스 성공")
    void resetPassword_process_success() throws Exception
    {
        // Given
        createAccount(email);

        // When & Then
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", profileDto.getEmail())
                        .param("password", profileDto.getPassword())
                        .param("remember-me", "true"))
                .andExpect(status().isOk());

        loginService.sendAuthenticationNumber(email, "RESET_PASSWORD");

        String code = codeRepository.findByEmail(email).get().getCode();
        loginService.resetPassword(
                ProfileDto.ResetPassword.builder()
                    .email(profileDto.getEmail())
                    .code(code)
                    .build()
        );

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", profileDto.getEmail())
                        .param("password", profileDto.getPassword())
                        .param("remember-me", "true"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("임시 비밀번호 발급 프로세스 실패 1 : 해당 이메일의 계정이 존재하지 않는 경우")
    void resetPassword_process_failure_1() throws Exception
    {
        // Given
        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        // When
        loginService.sendAuthenticationNumber(email, "RESET_PASSWORD");

        // Then
        assertThat(codeRepository.findByEmail(email)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("임시 비밀번호 발급 프로세스 실패 2 : 인증번호가 틀린 경우")
    void resetPassword_process_failure_2() throws Exception
    {
        // Given
        createAccount(email);

        // When & Then
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", profileDto.getEmail())
                        .param("password", profileDto.getPassword())
                        .param("remember-me", "true"))
                .andExpect(status().isOk());

        loginService.sendAuthenticationNumber(email, "RESET_PASSWORD");

        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.resetPassword(
                        ProfileDto.ResetPassword.builder()
                                .email(profileDto.getEmail())
                                .code("wrong_code")
                                .build()
                )
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.WRONG_CODE);
    }

    @Test
    @DisplayName("로그인 여부 확인 성공")
    void checkLogin_success()
    {
        // Given
        createAccount(email);

        Principal mockPrincipal = mock(Principal.class);
        given(mockPrincipal.getName()).willReturn(email);

        // When
        ProfileDto.CheckLogin result = loginService.checkLogin(mockPrincipal);

        // Then
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("로그인 여부 확인 실패 : 계정이 존재하지 않는 경우")
    void checkLogin_failure()
    {
        // Given
        Principal mockPrincipal = mock(Principal.class);
        given(mockPrincipal.getName()).willReturn("does_not_exist@naver.com");

        // When & Then
        AppException exception = assertThrows(
                AppException.class,
                () -> loginService.checkLogin(mockPrincipal)
        );
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.EMAIL_NOT_FOUND);
    }

    @Test
    @DisplayName("Access token 재발급 성공")
    void refreshToken_success() throws Exception
    {
        // Given
        createAccount(email);

        MvcResult loginResult = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", profileDto.getEmail())
                        .param("password", profileDto.getPassword())
                        .param("remember-me", "true"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = loginResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, new TypeReference<>() {});
        Map<String, String> dataMap = (Map<String, String>) responseMap.get("data");

        String refreshToken = dataMap.get("refreshToken");

        HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);
        given(mockHttpServletRequest.getHeader("Authorization")).willReturn("Bearer " + refreshToken);

        // When
        String newAccessToken = loginService.refreshToken(mockHttpServletRequest);

        // Then
        assertThat(newAccessToken).isNotEmpty();
        assertThat(newAccessToken).isNotNull();
    }

    @Test
    @DisplayName("Access token 재발급 실패 : 올바르지 않은 refresh token")
    void refreshToken_failure()
    {
        // Given
        String refreshToken = "thisIsNotOfficialRefreshToken";

        HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);
        given(mockHttpServletRequest.getHeader("Authorization")).willReturn("Bearer " + refreshToken);

        // When & Then
        assertThrows(
                AppException.class,
                () -> loginService.refreshToken(mockHttpServletRequest)
        );
    }
}