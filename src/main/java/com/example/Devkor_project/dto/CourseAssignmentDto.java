package com.example.Devkor_project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "시간표에 강의를 추가하기 위한 요청 DTO")
public class CourseAssignmentDto {

    @NotNull(message = "courseId는 null일 수 없습니다.")
    @Schema(description = "추가할 강의 ID")
    private Long courseId;
}
