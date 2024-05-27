package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.InsertCommentRequestDto;
import com.example.Devkor_project.dto.SignUpRequestDto;
import com.example.Devkor_project.dto.SuccessDto;
import com.example.Devkor_project.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /* 강의평 작성 시작 컨트롤러 */
    @GetMapping("/api/course/start-comment")
    public ResponseEntity<SuccessDto> startComment(Principal principal,
                                                   @RequestParam("course_id") Long course_id)
    {
        courseService.startComment(principal, course_id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        SuccessDto.builder()
                                .data(course_id)
                                .message("강의평 작성을 시작합니다.")
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* 강의평 작성 완료 및 등록 컨트롤러 */
    @PostMapping("/api/course/insert-comment")
    public ResponseEntity<SuccessDto> insertComment(Principal principal,
                                                    @Valid @RequestBody InsertCommentRequestDto dto)
    {
        courseService.insertComment(principal, dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        SuccessDto.builder()
                                .data(dto.getCourse_id())
                                .message("강의평 작성을 정상적으로 완료하였습니다.")
                                .version(versionProvider.getVersion())
                                .build()
                );
    }
}
