package com.example.Devkor_project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

public class NoticeDto
{
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class List
    {
        @NotNull(message = "[notice_id] cannot be null.")
        @Schema(description = "notice_id")
        private Long notice_id;

        @NotBlank(message = "[title] cannot be blank.")
        @Schema(description = "제목")
        private String title;

        @NotNull(message = "[created_at] cannot be null.")
        @Schema(description = "생성 날짜")
        private LocalDate created_at;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class Detail
    {
        @NotNull(message = "[notice_id] cannot be null.")
        @Schema(description = "notice_id")
        private Long notice_id;

        @NotBlank(message = "[title] cannot be blank.")
        @Schema(description = "제목")
        private String title;

        @NotBlank(message = "[detail] cannot be blank.")
        @Schema(description = "세부 내용")
        private String detail;

        @NotNull(message = "[created_at] cannot be null.")
        @Schema(description = "생성 날짜")
        private LocalDate created_at;

        @Schema(description = "대표 이미지 경로 1")
        private String image_1;
        @Schema(description = "대표 이미지 경로 2")
        private String image_2;
        @Schema(description = "대표 이미지 경로 3")
        private String image_3;
        @Schema(description = "대표 이미지 경로 4")
        private String image_4;
        @Schema(description = "대표 이미지 경로 5")
        private String image_5;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class Insert
    {
        @NotBlank(message = "[title] cannot be blank.")
        @Schema(description = "제목")
        private String title;

        @NotBlank(message = "[detail] cannot be blank.")
        @Schema(description = "세부 내용")
        private String detail;

        @Schema(description = "대표 이미지 경로 1")
        private String image_1;
        @Schema(description = "대표 이미지 경로 2")
        private String image_2;
        @Schema(description = "대표 이미지 경로 3")
        private String image_3;
        @Schema(description = "대표 이미지 경로 4")
        private String image_4;
        @Schema(description = "대표 이미지 경로 5")
        private String image_5;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class Update
    {
        @NotNull(message = "[notice_id] cannot be null.")
        @Schema(description = "notice_id")
        private Long notice_id;

        @NotBlank(message = "[title] cannot be blank.")
        @Schema(description = "제목")
        private String title;

        @NotBlank(message = "[detail] cannot be blank.")
        @Schema(description = "세부 내용")
        private String detail;

        @Schema(description = "대표 이미지 경로 1")
        private String image_1;
        @Schema(description = "대표 이미지 경로 2")
        private String image_2;
        @Schema(description = "대표 이미지 경로 3")
        private String image_3;
        @Schema(description = "대표 이미지 경로 4")
        private String image_4;
        @Schema(description = "대표 이미지 경로 5")
        private String image_5;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class Delete
    {
        @NotNull(message = "[notice_id] cannot be null.")
        @Schema(description = "notice_id")
        private Long notice_id;
    }
}
