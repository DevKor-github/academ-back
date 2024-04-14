package com.example.Devkor_project.controller;

import com.example.Devkor_project.dto.CourseDto;
import com.example.Devkor_project.dto.SearchCourseRequestDto;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.service.CheckAuthorityService;
import com.example.Devkor_project.service.HomeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
public class HomeController
{
    @Autowired
    HomeService homeService;

    @Autowired
    CheckAuthorityService checkAuthorityService;

    /*
        강의 검색 컨트롤러
    */
    @PostMapping("/api/search")
    public ResponseEntity<List<CourseDto>> searchCourse(@Valid @RequestBody SearchCourseRequestDto dto,
                                             HttpServletRequest request)
    {
        // USER 권한인지 확인
        checkAuthorityService.checkAuthority(request, "USER");

        List<CourseDto> courses = homeService.searchCourse(dto);

        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }
}
