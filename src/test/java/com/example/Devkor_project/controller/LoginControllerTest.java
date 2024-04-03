package com.example.Devkor_project.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Nested
@DisplayName("LoginController 테스트")
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Arrange
    String signUpRequestJson = "{" +
            "\"email\":\"test@gmail.com\"," +
            "\"password\":\"test\"," +
            "\"username\":\"test\"," +
            "\"studentId\":\"0000000000\"," +
            "\"grade\":1," +
            "\"semester\":1," +
            "\"department\":\"test\"" +
            "}";

    String loginRequestJson = "{" +
            "\"email\":\"test@gmail.com\"," +
            "\"password\":\"test\"," +
            "\"isSaved\":true" +
            "}";

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTest {

        @Test
        @Transactional
        @DisplayName("로그인 성공 테스트")
        public void testLogin_success() throws Exception {

            // Act & Assert
            mockMvc.perform(post("/api/login/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(signUpRequestJson));

            mockMvc.perform(post("/api/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(loginRequestJson))
                    .andExpect(status().isOk())
                    .andExpect(content().string("로그인이 정상적으로 수행되었습니다."))
                    .andDo(print());

        }
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class SignUpTest {

        @Test
        @Transactional
        @DisplayName("회원가입 성공 테스트")
        public void signUpTest_success() throws Exception {

            // Act & Assert
            mockMvc.perform(post("/api/login/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(signUpRequestJson))
                    .andExpect(status().isCreated())
                    .andExpect(content().string("회원가입을 성공하였습니다."))
                    .andDo(print());
        }
        @Test
        @Transactional
        @DisplayName("회원가입 실패 테스트 (중복된 요청)")
        public void signUpTest_failure() throws Exception {

            // Act & Assert
            mockMvc.perform(post("/api/login/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(signUpRequestJson));

            mockMvc.perform(post("/api/login/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(signUpRequestJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("EMAIL_DUPLICATED: " + "test@gmail.com" + "는 이미 사용 중입니다."))
                    .andDo(print());
        }
    }

}