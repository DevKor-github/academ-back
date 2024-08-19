package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController
{
    @Autowired VersionProvider versionProvider;

    @GetMapping("/health-check")
    public ResponseEntity<ResponseDto.Success> healthCheck()
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .data(null)
                        .message("로드 밸런서 대상 그룹 상태 검사 완료")
                        .version(versionProvider.getVersion())
                        .build()
                );
    }
}
