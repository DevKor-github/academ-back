package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.CourseDto;
import com.example.Devkor_project.dto.SuccessDto;
import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class HomeController
{
    @Autowired
    HomeService homeService;

    @Autowired
    VersionProvider versionProvider;

    /* 강의 검색 컨트롤러 */
    @GetMapping("/api/search")
    public ResponseEntity<SuccessDto> searchCourse(@RequestParam("keyword") String keyword)
    {
        List<Map<String, String>> courses = homeService.searchCourse(keyword);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessDto.builder()
                        .data(courses.toString())
                        .message("강의 검색이 성공적으로 수행되었습니다.")
                        .version(versionProvider.getVersion())
                        .build()
                );
    }
}
