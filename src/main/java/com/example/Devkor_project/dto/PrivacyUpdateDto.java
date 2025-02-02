package com.example.Devkor_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class PrivacyUpdateDto {
    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "요일은 필수 입력 항목입니다.")
    private String day;

    @NotNull(message = "시작 시간은 필수 입력 항목입니다.")
    private LocalTime startTime;

    @NotNull(message = "종료 시간은 필수 입력 항목입니다.")
    private LocalTime finishTime;

    private String location;
}
