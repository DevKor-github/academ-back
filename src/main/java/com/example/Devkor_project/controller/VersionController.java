package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.CommentDto;
import com.example.Devkor_project.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "버전", description = "버전 관련 api입니다.")
public class VersionController
{
    @Autowired VersionProvider versionProvider;

    /* 버전 확인 컨트톨러 */
    @GetMapping("/api/is-secure")
    @Operation(summary = "버전 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200 (HTTPS 요청 O)", description = "Connection is secure (HTTPS) 메시지를 반환합니다."),
            @ApiResponse(responseCode = "200 (HTTPS 요청 X)", description = "Connection is not secure (not HTTPS) 메시지를 반환합니다."),
    })
    public ResponseEntity<ResponseDto.Success> getHTTPSStatus(HttpServletRequest request)
    {
        boolean isHttps = request.isSecure();

        String message = isHttps ? "Connection is secure (HTTPS)." : "Connection is not secure (not HTTPS).";

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message(message)
                                .data(null)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }
}
