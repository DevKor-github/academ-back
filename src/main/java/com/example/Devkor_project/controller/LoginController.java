package com.example.Devkor_project.controller;

import com.example.Devkor_project.dto.SignUpRequestDto;
import com.example.Devkor_project.service.LoginService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class LoginController
{
    @Autowired
    private LoginService loginService;

    /* 로그인 성공 시 리다이렉트 경로 */
    @GetMapping("/")
    public ResponseEntity<String> login_success()
    {
        return ResponseEntity.status(HttpStatus.OK).body("로그인을 성공하였습니다.");
    }

    /* 회원가입 컨트롤러 */
    @PostMapping("/api/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDto dto)
    {
        loginService.signUp(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입을 성공하였습니다.");
    }

    /* 이메일 인증번호 발송 컨트롤러 */
    @PostMapping("/api/signup/send-email")
    public ResponseEntity<String> sendAuthenticationNumber(@RequestParam("email") String email)
    {
        loginService.sendAuthenticationNumber(email);
        return ResponseEntity.status(HttpStatus.OK).body("인증번호 이메일 발송에 성공하였습니다.");
    }

    /* 인증번호 확인 컨트롤러 */
    @GetMapping("/api/signup/check-email")
    public ResponseEntity<String> checkAuthenticationNumber(@RequestParam("email") String email,
                                                            @RequestParam("code") String code)
    {
        loginService.checkAuthenticationNumber(email, code);

        return ResponseEntity.status(HttpStatus.OK).body("인증번호 이메일 확인에 성공하였습니다.");
    }

    /* 임시 비밀번호 발급 컨트롤러 */
    @PostMapping("/api/login/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("email") String email)
    {
        loginService.resetPassword(email);
        return ResponseEntity.status(HttpStatus.OK).body("비밀번호가 성공적으로 초기화되었습니다.");
    }

}