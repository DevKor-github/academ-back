package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.PointDto;
import com.example.Devkor_project.dto.ProfileDto;
import com.example.Devkor_project.dto.ResponseDto;
import com.example.Devkor_project.service.PointService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;

@RestController
public class PointController {

    @Autowired PointService pointService;
    @Autowired VersionProvider versionProvider;

    /* 강의평 열람 권한 구매 컨트롤러 */
    @PostMapping("/api/point/buy-access-authority")
    public ResponseEntity<ResponseDto.Success> buyAccessAuthority(Principal principal,
                                                                  @Valid @RequestBody PointDto.Buy dto) {

        LocalDate expirationDate = pointService.buyAccessAuthority(principal, dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ResponseDto.Success.builder()
                                .message("강의평 열람 권한 구매를 성공하였습니다.")
                                .data(expirationDate)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }
}
