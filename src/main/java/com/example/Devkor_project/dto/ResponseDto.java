package com.example.Devkor_project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class ResponseDto
{
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Getter
    @Builder
    @Schema(description = "응답 성공 DTO")
    public static class Success
    {
        @Builder.Default
        @Schema(description = "응답 상태", example = "SUCCESS")
        private String status = "SUCCESS";

        @Schema(description = "메시지")
        private String message;

        @Schema(description = "데이터")
        private Object data;

        @Schema(description = "버전")
        private String version;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Getter
    @Builder
    @Schema(description = "응답 실패 DTO")
    public static class Error
    {
        @Builder.Default
        @Schema(description = "응답 상태", example = "ERROR")
        private String status = "ERROR";

        @Schema(description = "에러 코드")
        private String code;

        @Schema(description = "메시지")
        private String message;

        @Schema(description = "데이터")
        private Object data;

        @Schema(description = "버전")
        private String version;
    }
}
