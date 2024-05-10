package com.example.Devkor_project.controller;

import com.example.Devkor_project.dto.CourseDto;
import com.example.Devkor_project.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class HomeController
{
    @Autowired
    HomeService homeService;

    /*
        강의 검색 컨트롤러
    */
    @GetMapping("/api/search")
    public ResponseEntity<List<CourseDto>> searchCourse(@RequestParam("keyword") String keyword)
    {
        List<CourseDto> courses = homeService.searchCourse(keyword);

        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }
}
