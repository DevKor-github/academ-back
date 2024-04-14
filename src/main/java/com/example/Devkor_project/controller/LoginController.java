package com.example.Devkor_project.controller;

import com.example.Devkor_project.dto.EmailAuthenticationRequestDto;
import com.example.Devkor_project.dto.EmailCheckRequestDto;
import com.example.Devkor_project.dto.LoginRequestDto;
import com.example.Devkor_project.dto.SignUpRequestDto;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    /*
        < 로그인 컨트롤러 >
        LoginRequestDto를 받아서
        해당 이메일의 계정이 존재하지 않거나, 비밀번호가 일치하지 않는 경우: 실패 메시지(401) 응답
        예외가 발생하지 않는 경우: 세션 발행 후, 성공 메시지(200) 응답
    */
    @GetMapping("/api/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto dto,
                                        HttpServletRequest request)
    {
        loginService.login(dto, request);
        return ResponseEntity.status(HttpStatus.OK).body("로그인이 정상적으로 수행되었습니다.");
    }

    /*
        < 로그아웃 컨트롤러 >
        문제 없이 로그아웃에 성공한 경우: 세션 파기 후, 성공 메시지(200) 응답
        세션 파기 도중 예외가 발생한 경우: 실패 메시지(500) 응답
    */
    @GetMapping("/api/logout")
    public ResponseEntity<String> logout(HttpServletRequest request)
    {
        loginService.logout(request);
        return ResponseEntity.status(HttpStatus.OK).body("로그아웃이 정상적으로 수행되었습니다.");
    }

    /*
        < 회원가입 컨트롤러 >
        SignUpRequestDto를 받아서
        해당 이메일이 사용 중일 경우: 실패 메시지(400) 응답
        예외가 발생하지 않는 경우: profile 데이터베이스에 Profile 엔티티 추가 후, 성공 메시지(201) 응답
    */
    @PostMapping("/api/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDto dto)
    {
        loginService.signUp(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입을 성공하였습니다.");
    }

    /*
        < 이메일 인증번호 발송 컨트롤러 >
        EmailAuthenticationRequestDto를 받아서
        인증번호 이메일 전송에 성공한 경우: code 데이터베이스에 Code 엔티티 추가 후, 이메일 주소(200)를 응답
        인증번호 이메일 전송에 실패한 경우: 실패 메시지(500)를 응답
    */
    @PostMapping("/api/signup/email-authentication")
    public ResponseEntity<String> sendAuthenticationNumber(@Valid @RequestBody EmailAuthenticationRequestDto dto)
    {
        loginService.sendAuthenticationNumber(dto);
        return ResponseEntity.status(HttpStatus.OK).body(dto.getEmail());
    }

    /*
        < 인증번호 확인 컨트롤러 >
        EmailCheckRequestDto를 받아서
        인증번호가 맞는 경우: 이메일 주소(200) 응답
        해당 이메일이 사용 중일 경우: 실패 메시지(400) 응답
        해당 이메일로 발송된 인증번호가 없는 경우: 실패 메시지(500) 응답
        인증번호가 틀린 경우: 실패 메시지(401) 응답
    */
    @GetMapping("/api/signup/email-check")
    public ResponseEntity<String> checkAuthenticationNumber(@Valid @RequestBody EmailCheckRequestDto dto)
    {
        boolean isRight = loginService.checkAuthenticationNumber(dto);

        if(isRight)
            return ResponseEntity.status(HttpStatus.OK).body(dto.getEmail());
        else
            throw new AppException(ErrorCode.INVALID_CODE, "인증번호가 일치하지 않습니다.");
    }

    /*
        < 임시 비밀번호 발급 컨트롤러 >
        EmailAuthenticationRequestDto를 받아서
        임시 비밀번호 발급에 성공한 경우: profile 데이터베이스 수정 후, 성공 메시지(200) 응답
        해당 이메일로 생성된 계정이 존재하지 않을 경우: 실패 메시지(401) 응답
        이메일 전송에 실패한 경우: 실패 메시지(500) 응답
    */
    @PostMapping("/api/login/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody EmailAuthenticationRequestDto dto)
    {
        loginService.resetPassword(dto);
        return ResponseEntity.status(HttpStatus.OK).body("비밀번호가 성공적으로 초기화되었습니다.");
    }

}
