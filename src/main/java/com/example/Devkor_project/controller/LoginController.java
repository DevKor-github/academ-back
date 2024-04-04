package com.example.Devkor_project.controller;

import com.example.Devkor_project.dto.EmailAuthenticationRequestDto;
import com.example.Devkor_project.dto.LoginRequestDto;
import com.example.Devkor_project.dto.SignUpRequestDto;
import com.example.Devkor_project.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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

    /*
        < 로그인 Controller >
        LoginRequestDto를 받아서 service로 예외를 확인합니다.
        (해당 이메일로 생성된 계정이 존재하는지, 비밀번호가 일치하는지 확인)
        예외가 발생하지 않으면 세션을 발행합니다.
    */
    @PostMapping("")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto dto,
                                        HttpServletRequest request)
    {
        // 에러 여부 확인
        loginService.login(dto);

        // 세션 발행
        HttpSession session = request.getSession();
        session.setAttribute("email", dto.getEmail());
        if (dto.isSaved())
            session.setMaxInactiveInterval(1800);
        else
            session.setMaxInactiveInterval(1800000);

        return ResponseEntity.status(HttpStatus.OK).body("로그인이 정상적으로 수행되었습니다.");
    }

    /*
        < 회원가입 Controller >
        SignUpRequestDto를 받아서
        해당 이메일이 사용 중일 경우, 회원가입 실패 메시지(400 Bad Request)를 반환하고,
        해당 이메일이 사용 중이지 않으면, 회원가입 성공 메시지(201 Created)를 반환합니다.
    */
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDto dto)
    {
        loginService.signUp(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입을 성공하였습니다.");
    }

    /*
        < 이메일 인증번호 발송 Controller >
        EmailAuthenticationRequestDto를 받아서
        인증번호 이메일 전송에 성공한 경우, 인증번호에 100을 더한 숫자(200 OK)를 반환하고,
        인증번호 이메일 전송에 실패한 경우, 실패 메시지(500 Internal Server Error)를 반환합니다.
    */
    @PostMapping("/email-authentication")
    public ResponseEntity<Integer> sendAuthenticationNumber(@Valid @RequestBody EmailAuthenticationRequestDto dto)
    {
        String authenticationNumber = loginService.sendAuthenticationNumber(dto);
        int number = Integer.parseInt(authenticationNumber) + 100;
        return ResponseEntity.status(HttpStatus.OK).body(number);
    }

    /*
        < 임시 비밀번호 발급 Controller >
        EmailAuthenticationRequestDto를 받아서
        임시 비밀번호 발급에 성공한 경우, 성공 메시지(200 OK)를 반환하고,
        해당 이메일로 생성된 계정이 존재하지 않을 경우, 실패 메시지(401 Unauthorized)를 반환합니다.
    */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody EmailAuthenticationRequestDto dto)
    {
        loginService.resetPassword(dto);
        return ResponseEntity.status(HttpStatus.OK).body("비밀번호가 성공적으로 초기화되었습니다.");
    }

}
