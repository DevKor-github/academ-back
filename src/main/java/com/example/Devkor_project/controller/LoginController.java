package com.example.Devkor_project.controller;

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

    // 임시 비밀번호 발급
    @PostMapping("/reset-password")
    public ResponseEntity<String> restPassword()
    {
        return null;
    }

}
