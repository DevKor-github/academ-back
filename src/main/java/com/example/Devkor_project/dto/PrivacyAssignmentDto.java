package com.example.Devkor_project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Schema(description = "시간표에 개인 일정을 추가하기 위한 요청 DTO")
public class PrivacyAssignmentDto {

    @NotNull(message = "[privacyId] cannot be null.")
    @Schema(description = "추가할 개인 일정 ID")
    private Long privacyId;
}
