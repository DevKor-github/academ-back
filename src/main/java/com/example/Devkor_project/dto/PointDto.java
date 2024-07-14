package com.example.Devkor_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class PointDto {
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Builder
    public static class Buy
    {
        @NotBlank(message = "[item] cannot be blank.")
        private String item;
    }
}
