package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.NoticeDto;
import com.example.Devkor_project.dto.ResponseDto;
import com.example.Devkor_project.service.AdminService;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class AdminController {

        @Autowired
        AdminService adminService;

        @Autowired
        VersionProvider versionProvider;

        /* 대학원 강의 데이터베이스 초기화 컨트톨러 */
        @PostMapping("/api/admin/init-course-database")
        @JsonProperty("data") // data JSON 객체를 MAP<String, Object> 형식으로 매핑
        public ResponseEntity<ResponseDto.Success> initCourseDatabase(@Valid @RequestBody Map<String, Object> data) {
                adminService.initCourseDatabase(data);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("대학원 강의 데이터베이스 초기화가 정상적으로 수행되었습니다.")
                                        .data(null)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );

        }

        /* 대학원 강의 데이터베이스 추가 컨트톨러 */
        @PostMapping("/api/admin/insert-course-database")
        @JsonProperty("data") // data JSON 객체를 MAP<String, Object> 형식으로 매핑
        public ResponseEntity<ResponseDto.Success> insertCourseDatabase(@Valid @RequestBody Map<String, Object> data) {
                adminService.insertCourseDatabase(data);

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("대학원 강의 데이터베이스 추가가 정상적으로 수행되었습니다.")
                                        .data(null)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* 공지사항 추가 컨트톨러 */
        @PostMapping("/api/admin/insert-notice")
        public ResponseEntity<ResponseDto.Success> insertNotice(@Valid @RequestBody NoticeDto.Insert dto)
        {
                adminService.insertNotice(dto);

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("공지사항 추가가 정상적으로 수행되었습니다.")
                                        .data(null)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* 공지사항 수정 컨트톨러 */
        @PostMapping("/api/admin/update-notice")
        public ResponseEntity<ResponseDto.Success> updateNotice(@Valid @RequestBody NoticeDto.Update dto)
        {
                adminService.updateNotice(dto);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("공지사항 수정이 정상적으로 수행되었습니다.")
                                        .data(null)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* 공지사항 삭제 컨트톨러 */
        @PostMapping("/api/admin/delete-notice")
        public ResponseEntity<ResponseDto.Success> deleteNotice(@Valid @RequestBody NoticeDto.Delete dto)
        {
                adminService.deleteNotice(dto);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("공지사항 삭제가 정상적으로 수행되었습니다.")
                                        .data(null)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        @GetMapping("/api/is-secure")
        public ResponseEntity<ResponseDto.Success> getHTTPSStatus(HttpServletRequest request) {
                String st;

                boolean isHttps = request.isSecure();
                if (isHttps) {
                        st = "Connection is secure (HTTPS).";
                } else {
                        st = "Connection is not secure (not HTTPS).";
                }

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message(st)
                                        .data(null)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

}
