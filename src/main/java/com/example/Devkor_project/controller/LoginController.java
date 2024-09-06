package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.ProfileDto;
import com.example.Devkor_project.dto.ResponseDto;
import com.example.Devkor_project.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
public class LoginController {

        @Autowired LoginService loginService;
        @Autowired VersionProvider versionProvider;

        /* 회원가입 컨트롤러 */
        @PostMapping("/api/signup")
        public ResponseEntity<ResponseDto.Success> signUp(@Valid @RequestBody ProfileDto.Signup dto) {
                loginService.signUp(dto);

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("회원가입을 성공하였습니다.")
                                        .data(dto.getEmail())
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* 이메일 인증번호 발송 컨트롤러 */
        @GetMapping("/api/signup/send-email")
        public ResponseEntity<ResponseDto.Success> sendAuthenticationNumber(@RequestParam("email") String email) {
                loginService.sendAuthenticationNumber(email);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("인증번호 이메일 발송에 성공하였습니다.")
                                        .data(email)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* 인증번호 확인 컨트롤러 */
        @GetMapping("/api/signup/check-email")
        public ResponseEntity<ResponseDto.Success> checkAuthenticationNumber(@RequestParam("email") String email,
                                                                             @RequestParam("code") String code)
        {
                loginService.checkAuthenticationNumber(email, code);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("인증번호 이메일 확인에 성공하였습니다.")
                                        .data(email)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* 닉네임 중복 확인 컨트롤러 */
        @GetMapping("/api/signup/check-username")
        public ResponseEntity<ResponseDto.Success> checkUsername(@RequestParam("username") String username)
        {
                loginService.checkUsername(username);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("닉네임이 중복되지 않았습니다.")
                                        .data(null)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* 임시 비밀번호 발급 컨트롤러 */
        @PostMapping("/api/login/reset-password")
        public ResponseEntity<ResponseDto.Success> resetPassword(@Valid @RequestBody ProfileDto.ResetPassword dto)
        {
                loginService.resetPassword(dto);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("비밀번호가 성공적으로 초기화 되었습니다.")
                                        .data(dto.getEmail())
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* 로그인 여부 확인 컨트롤러 */
        @GetMapping("/api/check-login")
        public ResponseEntity<ResponseDto.Success> checkLogin(Principal principal)
        {
                ProfileDto.CheckLogin dto = loginService.checkLogin(principal);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("로그인 여부 확인이 완료되었습니다.")
                                        .data(dto)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* access token 재발급 컨트롤러 */
        @GetMapping("/api/refresh-token")
        public ResponseEntity<ResponseDto.Success> refreshToken(HttpServletRequest request)
        {
                String token = loginService.refreshToken(request);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("access token 재발급이 완료되었습니다.")
                                        .data(token)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

}
