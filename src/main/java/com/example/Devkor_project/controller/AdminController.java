package com.example.Devkor_project.controller;

import com.example.Devkor_project.dto.LoginRequestDto;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.service.AdminService;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
public class AdminController
{

    @Autowired
    AdminService adminService;

    // ADMIN 권한인지 체크하는 메서드
    void checkAuthority(HttpServletRequest request)
    {
        HttpSession session = request.getSession(false);
        if(session == null)
            throw new AppException(ErrorCode.NO_AUTHORITY, "권한이 없습니다.");
        else if(!Objects.equals(session.getAttribute("role").toString(), "ADMIN"))
            throw new AppException(ErrorCode.NO_AUTHORITY, "ADMIN 권한이 필요합니다.");
    }

    /*
        < 대학원 강의 데이터베이스 초기화 컨트톨러 >
        ADMIN 권한을 체크한 후,
        ADMIN 권한이라면 받은 3중첩의 대학원 강의 JSON 데이터를 Entities로 변환하여 course 데이터베이스를 초기화합니다.
    */
    @PostMapping("/api/admin/init-course-database")
    @JsonProperty("data")   // data JSON 객체를 MAP<String, Object> 형식으로 매핑
    public ResponseEntity<String> initCourseDatabase(@Valid @RequestBody Map<String,Object> data,
                                        HttpServletRequest request)
    {
        checkAuthority(request);

        adminService.initCourseDatabase(data);

        return ResponseEntity.status(HttpStatus.OK).body("대학원 강의 데이터베이스 초기화가 정상적으로 수행되었습니다.");
    }
}
