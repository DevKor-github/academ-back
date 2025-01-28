package com.example.Devkor_project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
@Schema(description = "응답 성공 DTO")
public class TimetableDto {

    @Schema(description = "Timetable ID")
    private Long id;

    @NotBlank(message = "[name] cannot be blank.")
    @Schema(description = "시간표 이름 (예: 25-1학기)")
    private String name;
}
