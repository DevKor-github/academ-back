package com.example.Devkor_project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class TrafficDto
{
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class Month
    {
        @NotBlank(message = "api_path는 빈 문자열일 수 없습니다.")
        @Schema(description = "api 경로")
        private String api_path;

        @NotNull(message = "count는 null일 수 없습니다.")
        @Schema(description = "요청 횟수")
        private int count;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class Year
    {
        @NotNull(message = "month는 null일 수 없습니다.")
        @Schema(description = "월")
        private Byte month;

        @NotNull(message = "count는 null일 수 없습니다.")
        @Schema(description = "총 요청 횟수")
        private int count;
    }
}
