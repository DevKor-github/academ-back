package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.NoticeDto;
import com.example.Devkor_project.dto.ResponseDto;
import com.example.Devkor_project.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NoticeController
{
    @Autowired NoticeService noticeService;
    @Autowired VersionProvider versionProvider;

    /* 공지사항 목록 조회 컨트롤러 */
    @GetMapping("/api/notice/list")
    public ResponseEntity<ResponseDto.Success> noticeList(@RequestParam("page") int page) {
        List<NoticeDto.List> data = noticeService.noticeList(page - 1);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("공지사항 목록 조회에 성공하였습니다.")
                                .data(data)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }
}
