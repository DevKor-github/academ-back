package com.example.Devkor_project.controller;

import com.example.Devkor_project.dto.SignUpRequestDto;
import com.example.Devkor_project.service.LoginService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@Slf4j
public class LoginController
{
    @Autowired
    private LoginService loginService;

    // 로그인
    @PostMapping()
    public ResponseEntity<String> login()
    {
        return null;
    }

    /*
        < 회원가입 Controller >
        SignUpRequestDto를 받아서
        해당 이메일이 사용 중일 경우, 회원가입 실패 메시지(400 Bad Request)를 반환하고,
        해당 이메일이 사용 중이지 않으면, 회원가입 성공 메시지(201 Created)를 반환합니다.
    */
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDto dto)
    {
        loginService.signUp(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입을 성공하였습니다.");
    }

    // 임시 비밀번호 발급
    @PostMapping("/reset-password")
    public ResponseEntity<String> restPassword()
    {
        return null;
    }

}
