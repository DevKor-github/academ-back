package com.example.Devkor_project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class PrivacyUpdateDto {
    //개인일정은 이름, 장소만 바꿀 수 있음
    //요일.시간 바꾸는건 아예 새로운 일정으로 "생성"하는게 깔끔
    //요일.시간 바꾸면... 모든 시간표에서 겹치지 않게 double check 하느라 비효율적
    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    @Schema(description = "개인 일정 이름")
    private String name;


    @Schema(description = "장소")
    private String location;
}
