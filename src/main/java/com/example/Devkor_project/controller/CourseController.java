package com.example.Devkor_project.controller;

import com.example.Devkor_project.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class CourseController
{
    @Autowired
    CourseService courseService;

    /* 강의 북마크 컨트롤러 */
    @GetMapping("/api/bookmark")
    public ResponseEntity<String> bookmark(Principal principal,
                                           @RequestParam("course_id") Long course_id)
    {
        courseService.bookmark(principal, course_id);

        return ResponseEntity.status(HttpStatus.OK).body("북마크가 정상적으로 수행되었습니다.");
    }
}
