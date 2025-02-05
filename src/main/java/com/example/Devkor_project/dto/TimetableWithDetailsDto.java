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
public class TimetableWithDetailsDto {

    private Long id;
    private Long profileId;
    private String name;
    private List<CourseDto.Simple> courses; // 연관된 Course 정보
    private List<PrivacyDto> privacies; // 연관된 Privacy 정보


}
