package com.example.Devkor_project.dto;

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
        private Long notice_id;

        @NotBlank(message = "[title] cannot be blank.")
        private String title;

        @NotNull(message = "[created_at] cannot be null.")
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
        private Long notice_id;

        @NotBlank(message = "[title] cannot be blank.")
        private String title;

        @NotBlank(message = "[detail] cannot be blank.")
        private String detail;

        @NotNull(message = "[created_at] cannot be null.")
        private LocalDate created_at;
    }
}
