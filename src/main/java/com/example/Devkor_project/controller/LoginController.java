package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.SignUpRequestDto;
import com.example.Devkor_project.dto.SuccessDto;
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
    LoginService loginService;

    @Autowired
    VersionProvider versionProvider;

    /* 로그인 성공 시 리다이렉트 경로 */
    @GetMapping("/")
    public ResponseEntity<SuccessDto> loginSuccess()
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessDto.builder()
                        .data(null)
                        .message("로그인을 성공하였습니다.")
                        .version(versionProvider.getVersion())
                        .build()
                );
    }

    /* 회원가입 컨트롤러 */
    @PostMapping("/api/signup")
    public ResponseEntity<SuccessDto> signUp(@Valid @RequestBody SignUpRequestDto dto)
    {
        loginService.signUp(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessDto.builder()
                        .data(dto.getEmail())
                        .message("회원가입을 성공하였습니다.")
                        .version(versionProvider.getVersion())
                        .build()
                );
    }

    /* 이메일 인증번호 발송 컨트롤러 */
    @PostMapping("/api/signup/send-email")
    public ResponseEntity<SuccessDto> sendAuthenticationNumber(@RequestParam("email") String email)
    {
        loginService.sendAuthenticationNumber(email);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessDto.builder()
                        .data(email + "@korea.ac.kr")
                        .message("인증번호 이메일 발송에 성공하였습니다.")
                        .version(versionProvider.getVersion())
                        .build()
                );
    }

    /* 인증번호 확인 컨트롤러 */
    @GetMapping("/api/signup/check-email")
    public ResponseEntity<SuccessDto> checkAuthenticationNumber(@RequestParam("email") String email,
                                                            @RequestParam("code") String code)
    {
        loginService.checkAuthenticationNumber(email, code);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessDto.builder()
                        .data(email + "@korea.ac.kr")
                        .message("인증번호 이메일 확인에 성공하였습니다.")
                        .version(versionProvider.getVersion())
                        .build()
                );
    }

    /* 임시 비밀번호 발급 컨트롤러 */
    @PostMapping("/api/login/reset-password")
    public ResponseEntity<SuccessDto> resetPassword(@RequestParam("email") String email)
    {
        loginService.resetPassword(email);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessDto.builder()
                        .data(email + "@korea.ac.kr")
                        .message("비밀번호가 성공적으로 초기화 되었습니다.")
                        .version(versionProvider.getVersion())
                        .build()
                );
    }

}
