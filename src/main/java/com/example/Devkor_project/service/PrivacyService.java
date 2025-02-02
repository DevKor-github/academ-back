package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.PrivacyDto;
import com.example.Devkor_project.dto.PrivacyUpdateDto;
import com.example.Devkor_project.dto.ResponseDto;
import com.example.Devkor_project.entity.Privacy;
import com.example.Devkor_project.entity.Timetable;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.PrivacyRepository;
import com.example.Devkor_project.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivacyService {

    private final PrivacyRepository privacyRepository;
    private final TimetableRepository timetableRepository;

    // 특정 시간표의 개인일정 조회
    @Transactional
    public List<PrivacyDto> getPrivacyByTimetable(Long timetableId, Principal principal) {
        validateTimetableOwnership(timetableId, principal);
        return privacyRepository.findByTimetables_Id(timetableId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 사용자의 모든 개인일정 조회
    @Transactional
    public List<PrivacyDto> getAllPrivacy(Principal principal) {
        String userEmail = principal.getName();

        List<Long> timetableIds = timetableRepository.findAllByProfile_Email(userEmail)
                .stream()
                .map(Timetable::getId)
                .collect(Collectors.toList());

        return privacyRepository.findByTimetables_IdIn(timetableIds).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 개인일정 생성
    @Transactional
    public PrivacyDto createPrivacy(PrivacyDto privacyDto, Principal principal) {
        List<Timetable> timetables = validateTimetablesOwnership(privacyDto.getTimetableIds(), principal);

        Privacy privacy = Privacy.builder()
                .timetables(timetables)
                .name(privacyDto.getName())
                .day(privacyDto.getDay())
                .startTime(privacyDto.getStartTime())
                .finishTime(privacyDto.getFinishTime())
                .location(privacyDto.getLocation())
                .build();

        return convertToDto(privacyRepository.save(privacy));
    }

    // 개인일정 수정
    @Transactional
    public ResponseEntity<ResponseDto.Success> updatePrivacy(Long privacyId, PrivacyUpdateDto updateDto, Principal principal) {
        Privacy privacy = privacyRepository.findById(privacyId)
                .orElseThrow(() -> new AppException(ErrorCode.PRIVACY_NOT_FOUND, "해당 개인 일정을 찾을 수 없습니다."));

        validateTimetablesOwnership(privacy.getTimetables().stream()
                .map(Timetable::getId)
                .collect(Collectors.toList()), principal);

        // 기존 일정 정보 업데이트
        privacy.setName(updateDto.getName());
        privacy.setLocation(updateDto.getLocation());

        privacyRepository.save(privacy);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("개인 일정이 성공적으로 수정되었습니다.")
                        .data(convertToDto(privacy))
                        .version("v1.1.4")
                        .build());
    }


    // 개인일정 삭제
    @Transactional
    public void deletePrivacy(Long id, Principal principal) {
        Privacy privacy = privacyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Privacy not found"));

        validateTimetablesOwnership(privacy.getTimetables().stream()
                .map(Timetable::getId)
                .collect(Collectors.toList()), principal);

        privacyRepository.deleteById(id);
    }

    // 특정 시간표에 개인일정 추가
    @Transactional
    public PrivacyDto addPrivacyToTimetable(Long timetableId, PrivacyDto privacyDto, Principal principal) {
        Timetable timetable = validateTimetableOwnership(timetableId, principal);

        Privacy privacy = Privacy.builder()
                .name(privacyDto.getName())
                .day(privacyDto.getDay())
                .startTime(privacyDto.getStartTime())
                .finishTime(privacyDto.getFinishTime())
                .location(privacyDto.getLocation())
                .build();

        privacy.getTimetables().add(timetable);
        return convertToDto(privacyRepository.save(privacy));
    }

    // 특정 시간표에서 개인일정 삭제
    @Transactional
    public void removePrivacyFromTimetable(Long timetableId, Long privacyId, Principal principal) {
        validateTimetableOwnership(timetableId, principal);

        Privacy privacy = privacyRepository.findById(privacyId)
                .orElseThrow(() -> new RuntimeException("Privacy not found"));

        privacy.getTimetables().removeIf(timetable -> timetable.getId().equals(timetableId));
        privacyRepository.save(privacy);
    }

    // 시간표 소유권 확인
    private Timetable validateTimetableOwnership(Long timetableId, Principal principal) {
        return timetableRepository.findById(timetableId)
                .filter(timetable -> timetable.getProfile().getEmail().equals(principal.getName()))
                .orElseThrow(() -> new RuntimeException("You do not have access to this timetable"));
    }

    // 여러 시간표 소유권 확인
    private List<Timetable> validateTimetablesOwnership(List<Long> timetableIds, Principal principal) {
        List<Timetable> timetables = timetableRepository.findAllById(timetableIds);
        boolean isOwner = timetables.stream()
                .allMatch(timetable -> timetable.getProfile().getEmail().equals(principal.getName()));

        if (!isOwner) {
            throw new RuntimeException("You do not have access to one or more timetables");
        }

        return timetables;
    }

    private PrivacyDto convertToDto(Privacy privacy) {
        return PrivacyDto.builder()
                .id(privacy.getId())
                .timetableIds(privacy.getTimetables().stream()
                        .map(Timetable::getId)
                        .collect(Collectors.toList()))
                .name(privacy.getName())
                .day(privacy.getDay())
                .startTime(privacy.getStartTime())
                .finishTime(privacy.getFinishTime())
                .location(privacy.getLocation())
                .createdAt(privacy.getCreatedAt())
                .updatedAt(privacy.getUpdatedAt())
                .build();
    }
}
