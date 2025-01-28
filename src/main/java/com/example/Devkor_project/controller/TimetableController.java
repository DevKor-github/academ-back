package com.example.Devkor_project.controller;

import com.example.Devkor_project.dto.PrivacyDto;
import com.example.Devkor_project.dto.TimetableDto;
import com.example.Devkor_project.dto.ResponseDto;
import com.example.Devkor_project.entity.Timetable;
import com.example.Devkor_project.service.TimetableService;
import com.example.Devkor_project.configuration.VersionProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/timetables")
@RequiredArgsConstructor
@Tag(name = "Timetable 시간표", description = "시간표(timetable) 관련 API입니다.")
public class TimetableController {

    private final TimetableService timetableService;
    private final VersionProvider versionProvider;

    /** 🟢 로그인된 사용자 시간표 생성 */
    @PostMapping
    @Operation(summary = "시간표 생성")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
    public ResponseEntity<ResponseDto.Success> createTimetable(
            @Valid @RequestBody TimetableDto timetableDto,
            Principal principal
    ) {
        Timetable timetable = timetableService.createTimetableForProfile(timetableDto, principal);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("시간표가 성공적으로 생성되었습니다.")
                        .data(timetable)
                        .version(versionProvider.getVersion())
                        .build());
    }

    /** 🟢 강의 추가 */
    @PostMapping("/{timetableId}/course")
    @Operation(summary = "시간표에 강의 추가")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
    public ResponseEntity<?> addCourseToTimetable(@PathVariable Long timetableId, @RequestParam Long courseId, Principal principal) {
        return timetableService.addCourseToTimetable(timetableId, courseId, principal);
    }

    /** 🟢 강의 제거 */
    @DeleteMapping("/{timetableId}/course/{courseId}")
    @Operation(summary = "시간표에서 강의 제거")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
    public ResponseEntity<?> removeCourseFromTimetable(@PathVariable Long timetableId, @PathVariable Long courseId, Principal principal) {
        return timetableService.removeCourseFromTimetable(timetableId, courseId, principal);
    }

    /** 🟢 개인 일정 추가 */
    @PostMapping("/{timetableId}/privacy")
    @Operation(summary = "시간표에 개인 일정 추가")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
    public ResponseEntity<ResponseDto.Success> addPrivacyToTimetable(@PathVariable Long timetableId, @Valid @RequestBody PrivacyDto privacyDto, Principal principal) {
        return timetableService.addPrivacyToTimetable(timetableId, privacyDto, principal);
    }

    /** 🟢 로그인된 사용자의 모든 시간표 조회 */
    @GetMapping
    @Operation(summary = "로그인된 사용자의 모든 시간표 조회")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")

    public ResponseEntity<ResponseDto.Success> getAllTimetablesForUser(Principal principal) {
        return timetableService.getAllTimetablesForUser(principal);
    }

    /** 🟢 로그인된 사용자의 시간표 이름과 ID 조회 */
    @GetMapping("/names-and-ids")
    @Operation(summary = "로그인된 사용자의 시간표 이름과 ID 조회")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
    public ResponseEntity<ResponseDto.Success> getTimetableNamesAndIds(Principal principal) {
        return timetableService.getTimetableNamesAndIds(principal);
    }

    /** 🟢 특정 시간표 조회 */
    @GetMapping("/{timetableId}")
    @Operation(summary = "특정 시간표 조회")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
    public ResponseEntity<ResponseDto.Success> getTimetableById(@PathVariable Long timetableId, Principal principal) {
        return timetableService.getTimetableById(timetableId, principal);
    }

    /** 🟢 시간표에서 개인 일정 제거 */
    @DeleteMapping("/{timetableId}/privacy/{privacyId}")
    @Operation(summary = "시간표에서 개인 일정 제거")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
    public ResponseEntity<ResponseDto.Success> removePrivacyFromTimetable(
            @PathVariable Long timetableId,
            @PathVariable Long privacyId,
            Principal principal
    ) {
        return timetableService.removePrivacyFromTimetable(timetableId, privacyId, principal);
    }

    /** 🟢 시간표 이름 변경 */
    @PutMapping("/{timetableId}")
    @Operation(summary = "시간표 이름 변경")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
    @Parameter(in = ParameterIn.PATH, name = "timetableId", description = "이름을 변경할 시간표 ID")
    public ResponseEntity<ResponseDto.Success> updateTimetableName(
            @PathVariable Long timetableId,
            @RequestBody TimetableDto timetableDto,
            Principal principal
    ) {
        return timetableService.updateTimetableName(timetableId, timetableDto, principal);
    }
}
