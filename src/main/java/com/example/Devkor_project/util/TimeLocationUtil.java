package com.example.Devkor_project.util;
import com.example.Devkor_project.dto.TimeLocationDto;
import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.entity.Privacy;
import com.example.Devkor_project.entity.TimeLocation;
import com.example.Devkor_project.dto.TimetableDto;
import com.example.Devkor_project.entity.Timetable;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.TimeLocationRepository;

import java.util.ArrayList;
import java.util.List;
public class TimeLocationUtil {

    // 🕒 시간표에서 기존 강의 & 개인 일정 가져오기
    public static List<TimeLocationDto> extractFromTimetable(Timetable timetable, TimeLocationRepository timeLocationRepository) {
        List<TimeLocationDto> existingTimeLocations = new ArrayList<>(); // 빈 리스트 생성 (가변 가능)

        // 📌 기존 강의의 시간표 정보 가져오기
        List<TimeLocation> courseTimeLocations = timeLocationRepository.findByCourseIds(
                timetable.getCourses().stream().map(Course::getCourse_id).toList()
        );

        existingTimeLocations.addAll(courseTimeLocations.stream()
                .map(tl -> new TimeLocationDto(tl.getDay(), tl.getStartPeriod(), tl.getEndPeriod(), tl.getLocation()))
                .toList());

        // 📌 기존 개인 일정의 시간 정보 추가
        existingTimeLocations.addAll(timetable.getPrivacies().stream()
                .map(p -> new TimeLocationDto(p.getDay(), p.getStartTime().getHour(), p.getFinishTime().getHour(), p.getLocation()))
                .toList());

        return existingTimeLocations;
    }


    // ⚠️ **스케줄 충돌 여부 확인 함수 추가**
    public static boolean hasScheduleConflict(List<TimeLocationDto> existingSchedules, List<TimeLocationDto> newSchedules) {
        for (TimeLocationDto existing : existingSchedules) {
            for (TimeLocationDto newSchedule : newSchedules) {
                if (existing.getDay().equals(newSchedule.getDay()) &&
                        isTimeOverlapping(existing.getStartPeriod(), existing.getEndPeriod(),
                                newSchedule.getStartPeriod(), newSchedule.getEndPeriod())) {
                    return true;
                }
            }
        }
        return false;
    }

    // ⏳ 시간이 겹치는지 확인
    private static boolean isTimeOverlapping(int start1, int end1, int start2, int end2) {
        return start1 <= end2 && start2 <= end1;
    }
}