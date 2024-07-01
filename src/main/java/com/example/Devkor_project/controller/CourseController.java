package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.*;
import com.example.Devkor_project.service.CourseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class CourseController
{
    @Autowired
    CourseService courseService;

    @Autowired
    VersionProvider versionProvider;

    /* 강의 검색 컨트롤러 */
    @GetMapping("/api/course/search")
    public ResponseEntity<ResponseDto.Success> searchCourse(@RequestParam("keyword") String keyword)
    {
        List<CourseDto> courses = courseService.searchCourse(keyword);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("강의 검색이 성공적으로 수행되었습니다.")
                        .data(courses)
                        .version(versionProvider.getVersion())
                        .build()
                );
    }

    /* 강의 상세 정보 컨트롤러 */
    @GetMapping("api/course/detail")
    public ResponseEntity<ResponseDto.Success> courseDetail(@RequestParam("course_id") Long course_id)
    {
        CourseDetailDto dto = courseService.courseDetail(course_id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("강의 상세 정보가 성공적으로 반환되었습니다.")
                                .data(dto)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* 강의 북마크 컨트롤러 */
    @GetMapping("/api/course/bookmark")
    public ResponseEntity<ResponseDto.Success> bookmark(Principal principal,
                                               @RequestParam("course_id") Long course_id)
    {
        courseService.bookmark(principal, course_id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("북마크가 정상적으로 수행되었습니다.")
                                .data(null)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* 강의평 작성 시작 컨트롤러 */
    @GetMapping("/api/course/start-insert-comment")
    public ResponseEntity<ResponseDto.Success> startInsertComment(Principal principal,
                                                   @RequestParam("course_id") Long course_id)
    {
        courseService.startInsertComment(principal, course_id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("강의평 작성을 시작합니다.")
                                .data(course_id)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* 강의평 작성 완료 및 등록 컨트롤러 */
    @PostMapping("/api/course/insert-comment")
    public ResponseEntity<ResponseDto.Success> insertComment(Principal principal,
                                                    @Valid @RequestBody CommentDto.Insert dto)
    {
        courseService.insertComment(principal, dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ResponseDto.Success.builder()
                                .message("강의평 작성을 정상적으로 완료하였습니다.")
                                .data(dto.getCourse_id())
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* 강의평 수정 시작 컨트롤러 */
    @GetMapping("/api/course/start-update-comment")
    public ResponseEntity<ResponseDto.Success> startUpdateComment(Principal principal,
                                                            @RequestParam("comment_id") Long comment_id)
    {
        CommentDto.Update dto = courseService.startUpdateComment(principal, comment_id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("강의평 수정을 시작합니다.")
                                .data(dto)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* 강의평 수정 완료 컨트롤러 */
    @PostMapping("/api/course/update-comment")
    public ResponseEntity<ResponseDto.Success> updateComment(Principal principal,
                                                             @Valid @RequestBody CommentDto.Update dto)
    {
        Long course_id = courseService.updateComment(principal, dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("강의평 수정을 정상적으로 완료하였습니다.")
                                .data(course_id)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* 강의평 삭제 컨트롤러 */
    @PostMapping("/api/course/delete-comment")
    public ResponseEntity<ResponseDto.Success> deleteComment(Principal principal,
                                                             @Valid @RequestBody CommentDto.Delete dto)
    {
        Long course_id = courseService.deleteComment(principal, dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("강의평 삭제를 정상적으로 완료하였습니다.")
                                .data(course_id)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* 내가 작성한 강의평 전체 조회 컨트롤러 */
    @GetMapping("/api/course/my-comments")
    public ResponseEntity<ResponseDto.Success> myComments(Principal principal)
    {
        List<CommentDto.Comment> my_comments = courseService.myComments(principal);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("내가 작성한 강의평 전체 조회를 정상적으로 완료하였습니다.")
                                .data(my_comments)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }
}
