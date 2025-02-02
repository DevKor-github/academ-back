package com.example.Devkor_project.controller;

import com.example.Devkor_project.dto.PrivacyDto;
import com.example.Devkor_project.dto.PrivacyUpdateDto;
import com.example.Devkor_project.dto.ResponseDto;
import com.example.Devkor_project.service.PrivacyService;
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
import java.util.List;

@RestController
@RequestMapping("/api/privacy")
@RequiredArgsConstructor
@Tag(name = "Privacy 개인일정", description = "개인일정(privacy) 관련 API입니다.")
public class PrivacyController {

    private final PrivacyService privacyService;
    private final VersionProvider versionProvider;

    @GetMapping
    @Operation(summary = "해당 사용자의 모든 개인일정 조회")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
    public List<PrivacyDto> getAllPrivacy(Principal principal) {
        return privacyService.getAllPrivacy(principal);
    }

    @GetMapping("/timetable/{timetableId}")
    @Operation(summary = "특정 시간표의 개인일정 조회")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
    @Parameter(in = ParameterIn.PATH, name = "timetableId", description = "조회할 시간표 ID")
    public List<PrivacyDto> getPrivacyByTimetable(@PathVariable ("timetableId")Long timetableId, Principal principal) {
        return privacyService.getPrivacyByTimetable(timetableId, principal);
    }


//    @PostMapping
//    @Operation(summary = "개인일정 생성")
//    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
//    public PrivacyDto createPrivacy(@Valid @RequestBody PrivacyDto privacyDto, Principal principal) {
//        return privacyService.createPrivacy(privacyDto, principal);
//    }

    @PostMapping
    @Operation(summary = "개인일정 생성(요일이름 반드시 한글자로 할것!!! ex. 월, 수, 토)")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
    public ResponseEntity<ResponseDto.Success> createPrivacy(@Valid @RequestBody PrivacyDto privacyDto, Principal principal) {
        PrivacyDto createdPrivacy = privacyService.createPrivacy(privacyDto, principal);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("개인일정이 성공적으로 생성되었습니다.")
                        .data(createdPrivacy)
                        .version(versionProvider.getVersion())
                        .build()
                );
    }

    @PutMapping("/{privacyId}")
    @Operation(summary = "개인일정 수정 (이름 & 장소만 변경 가능)")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
    @Parameter(in = ParameterIn.PATH, name = "privacyId", description = "수정할 개인일정 ID")
    public ResponseEntity<ResponseDto.Success> updatePrivacy(@PathVariable ("privacyId")Long privacyId, @Valid @RequestBody PrivacyUpdateDto privacyUpdateDto, Principal principal) {
        return privacyService.updatePrivacy(privacyId, privacyUpdateDto, principal);
    }

    @DeleteMapping("/{privacyId}")
    @Operation(summary = "개인일정 삭제")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
    public ResponseEntity<ResponseDto.Success> deletePrivacy(@PathVariable ("privacyId")Long privacyId, Principal principal) {
        privacyService.deletePrivacy(privacyId, principal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("개인 일정이 성공적으로 삭제되었습니다.")
                        .version("v1.2.1-alpha")
                        .build());

    }

//    @PostMapping("/{timetableId}/privacy")
//    @Operation(summary = "특정 시간표에 개인일정 추가(아래 timetable쪽에 똑같은 api있어서 삭제)")
//    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
//    public PrivacyDto addPrivacyToTimetable(@PathVariable Long timetableId, @Valid @RequestBody PrivacyDto privacyDto, Principal principal) {
//        return privacyService.addPrivacyToTimetable(timetableId, privacyDto, principal);
//    }

//    @DeleteMapping("/{timetableId}/privacy/{privacyId}")
//    @Operation(summary = "특정 시간표에서 개인일정 삭제(아래 timetable쪽에 똑같은 api있어서 삭제)")
//    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
//    public void removePrivacyFromTimetable(@PathVariable Long timetableId, @PathVariable Long privacyId, Principal principal) {
//        privacyService.removePrivacyFromTimetable(timetableId, privacyId, principal);
//    }
}
