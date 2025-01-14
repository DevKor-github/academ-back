package com.example.Devkor_project.controller;

import com.example.Devkor_project.dto.PrivacyDto;
import com.example.Devkor_project.entity.Timetable;
import com.example.Devkor_project.service.TimetableService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/timetables")
@RequiredArgsConstructor
@Tag(name = "Timetable 시간표", description = "시간표(timetable) 관련 api입니다.")
public class TimetableController {

    private final TimetableService timetableService;

    @PostMapping("/profile/{username}")
    public Timetable createTimetable(@PathVariable String username) {
        return timetableService.createTimetableForProfile(username);
    }

    @PostMapping("/{id}/course")
    public void addCourseToTimetable(@PathVariable Long id, @RequestParam Long courseId) {
        timetableService.addCourseToTimetable(id, courseId);
    }

    @DeleteMapping("/{id}/course/{courseId}")
    public void removeCourseFromTimetable(@PathVariable Long id, @PathVariable Long courseId) {
        timetableService.removeCourseFromTimetable(id, courseId);
    }

    @PostMapping("/{id}/privacy")
    public void addPrivacyToTimetable(@PathVariable Long id, @RequestBody PrivacyDto privacyDto) {
        timetableService.addPrivacyToTimetable(id, privacyDto);
    }

    @DeleteMapping("/{id}/privacy/{privacyId}")
    public void removePrivacyFromTimetable(@PathVariable Long id, @PathVariable Long privacyId) {
        timetableService.removePrivacyFromTimetable(id, privacyId);
    }

    @GetMapping("/profile/{profileId}")
    public List<Timetable> getTimetableByProfile(@PathVariable Long profileId) {
        return timetableService.getTimetableByProfileId(profileId);
    }

}

