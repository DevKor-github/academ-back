package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.SuccessDto;
import com.example.Devkor_project.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class CourseController
{
    @Autowired
    CourseService courseService;

    @Autowired
    VersionProvider versionProvider;

    /* 강의 검색 컨트롤러 */
    @GetMapping("/api/course/search")
    public ResponseEntity<SuccessDto> searchCourse(@RequestParam("keyword") String keyword)
    {
        List<Map<String, Object>> courses = courseService.searchCourse(keyword);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessDto.builder()
                        .data(courses)
                        .message("강의 검색이 성공적으로 수행되었습니다.")
                        .version(versionProvider.getVersion())
                        .build()
                );
    }

    /* 강의 북마크 컨트롤러 */
    @GetMapping("/api/course/bookmark")
    public ResponseEntity<SuccessDto> bookmark(Principal principal,
                                               @RequestParam("course_id") Long course_id)
    {
        courseService.bookmark(principal, course_id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        SuccessDto.builder()
                                .data(null)
                                .message("북마크가 정상적으로 수행되었습니다.")
                                .version(versionProvider.getVersion())
                                .build()
                );
    }
}
