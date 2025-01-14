package com.example.Devkor_project.controller;

import com.example.Devkor_project.dto.PrivacyDto;
import com.example.Devkor_project.service.PrivacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/privacy")
@RequiredArgsConstructor
@Tag(name = "Privacy 개인일정", description = "개인일정(privacy) 관련 api입니다.")
public class PrivacyController {

    private final PrivacyService privacyService;

    @GetMapping
    public List<PrivacyDto> getAllPrivacy(@RequestParam Long timetableId) {
        return privacyService.getAllPrivacy(timetableId);
    }

    @PostMapping
    public PrivacyDto createPrivacy(@Valid @RequestBody PrivacyDto privacyDto) {
        return privacyService.createPrivacy(privacyDto);
    }

    @PutMapping("/{id}")
    public PrivacyDto updatePrivacy(@PathVariable Long id, @Valid @RequestBody PrivacyDto privacyDto) {
        return privacyService.updatePrivacy(id, privacyDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "개인일정 삭제")
    public void deletePrivacy(@PathVariable Long id) {
        privacyService.deletePrivacy(id);
    }

    @PostMapping("/{timetableId}/privacy")
    public PrivacyDto addPrivacyToTimetable(@PathVariable Long timetableId, @Valid @RequestBody PrivacyDto privacyDto) {
        return privacyService.addPrivacyToTimetable(timetableId, privacyDto);
    }

    @DeleteMapping("/{timetableId}/privacy/{privacyId}")
    @Operation(summary = "해당 timetable에서 개인일정 삭제")
    public void removePrivacyFromTimetable(@PathVariable Long timetableId, @PathVariable Long privacyId) {
        privacyService.removePrivacyFromTimetable(timetableId, privacyId);
    }

}
