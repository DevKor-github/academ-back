package com.example.Devkor_project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.example.Devkor_project.entity.Privacy;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PrivacyDto {

    @Schema(description = "Privacy ID")
    private Long id;

//    @NotNull(message = "[timetableId] cannot be null... 널이어도 되나? 되는듯")

    @Schema(description = "Timetable IDs") // Timetable과 연결된 ID
    private List<Long> timetableIds;

    @NotBlank(message = "[name] cannot be blank.")
    @Schema(description = "개인 일정 이름")
    private String name;

    @NotBlank(message = "[day] cannot be blank.")
    @Schema(description = "요일 (e.g., 월, 화, 수)")
    private String day;

    @NotNull(message = "[startTime] cannot be null.")
    @Schema(description = "시작 시간")
    private LocalTime startTime;

    @NotNull(message = "[finishTime] cannot be null.")
    @Schema(description = "종료 시간")
    private LocalTime finishTime;

    @Schema(description = "장소")
    private String location;

    @Schema(description = "생성 시간")
    private LocalDateTime createdAt;

    @Schema(description = "수정 시간")
    private LocalDateTime updatedAt;

    public static PrivacyDto fromPrivacy(Privacy privacy) {
        return PrivacyDto.builder()
                .id(privacy.getId())
                .name(privacy.getName())
                .day(privacy.getDay())
                .startTime(privacy.getStartTime())
                .finishTime(privacy.getFinishTime())
                .location(privacy.getLocation())
                .build();
    }
}
