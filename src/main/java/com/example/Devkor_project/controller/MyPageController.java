package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.ProfileDto;
import com.example.Devkor_project.dto.ResponseDto;
import com.example.Devkor_project.service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class MyPageController {

    @Autowired MyPageService myPageService;
    @Autowired VersionProvider versionProvider;

    /* 마이페이지 확인 컨트롤러 */
    @GetMapping("/api/mypage")
    public ResponseEntity<ResponseDto.Success> myPage(Principal principal)
    {
        ProfileDto.MyPage dto = myPageService.myPage(principal);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("마이페이지 불러오기가 성공적으로 수행되었습니다.")
                        .data(dto)
                        .version(versionProvider.getVersion())
                        .build()
                );
    }
}
