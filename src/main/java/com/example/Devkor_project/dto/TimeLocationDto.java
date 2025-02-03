package com.example.Devkor_project.dto;

import com.example.Devkor_project.entity.TimeLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeLocationDto {
    private String day;        // 요일 (월, 화, 수 등)
    private Integer startPeriod; // 시작 교시 (or 시작 시간)
    private Integer endPeriod;   // 끝 교시 (or 끝 시간)
    private String location;     // 장소 (강의실 or 개인 일정 장소)

    public static TimeLocationDto fromEntity(TimeLocation timeLocation) {
        return TimeLocationDto.builder()
                .day(timeLocation.getDay())
                .startPeriod(timeLocation.getStartPeriod())
                .endPeriod(timeLocation.getEndPeriod())
                .location(timeLocation.getLocation())
                .build();
    }
}
