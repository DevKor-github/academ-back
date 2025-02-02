package com.example.Devkor_project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CourseAssignmentDto {

    @NotNull(message = "courseId는 null일 수 없습니다.")
    @Schema(description = "추가할 강의 ID")
    private Long courseId;
}
