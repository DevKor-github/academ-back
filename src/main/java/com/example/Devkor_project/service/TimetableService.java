package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.*;
import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.entity.Privacy;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.entity.Timetable;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.CourseRepository;
import com.example.Devkor_project.repository.PrivacyRepository;
import com.example.Devkor_project.repository.ProfileRepository;
import com.example.Devkor_project.repository.TimetableRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final CourseRepository courseRepository;
    private final PrivacyRepository privacyRepository;
    private final ProfileRepository profileRepository;

    /**
     * 로그인된 사용자의 시간표 생성
     */
    @Transactional
    public Timetable createTimetableForProfile(TimetableDto timetableDto, Principal principal) {
        Profile profile = getProfileByPrincipal(principal);

        // Timetable 생성
        Timetable timetable = Timetable.builder()
                .profile(profile)
                .name(timetableDto.getName())
                .build();

        return timetableRepository.save(timetable);
    }

    /**
     * 로그인된 사용자의 모든 시간표 조회
     */
    @Transactional
    public ResponseEntity<ResponseDto.Success> getAllTimetablesForUser(Principal principal) {
        Profile profile = getProfileByPrincipal(principal);

        List<Timetable> timetables = timetableRepository.findByProfile_profileId(profile.getProfile_id());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("모든 시간표를 성공적으로 조회하였습니다.")
                        .data(timetables)
                        .version("v1.1.4")
                        .build());
    }

    /**
     * 로그인된 사용자의 시간표 이름과 ID 조회
     */
    @Transactional
    public ResponseEntity<ResponseDto.Success> getTimetableNamesAndIds(Principal principal) {
        Profile profile = getProfileByPrincipal(principal);

        List<TimetableDto> timetableDtos = timetableRepository.findByProfile_profileId(profile.getProfile_id()).stream()
                .map(t -> new TimetableDto(t.getId(), t.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("시간표 이름과 ID를 성공적으로 조회하였습니다.")
                        .data(timetableDtos)
                        .version("v1.1.4")
                        .build());
    }

    /**
     * 특정 시간표 조회 (상세 정보 포함)
     */
    @Transactional
    public ResponseEntity<ResponseDto.Success> getTimetableByIdWithDetails(Long timetableId, Principal principal) {
        Timetable timetable = validateProfileOwnership(timetableId, principal);

        // DTO 변환
        TimetableWithDetailsDto dto = TimetableWithDetailsDto.builder()
                .id(timetable.getId())
                .profileId(timetable.getProfile().getProfile_id())
                .name(timetable.getName())
                .courses(timetable.getCourses().stream()
                        .map(CourseDto::fromCourse)  // ✅ CourseDto 변환 메서드 사용
                        .collect(Collectors.toList()))
                .privacies(timetable.getPrivacies().stream()
                        .map(PrivacyDto::fromPrivacy)  // ✅ PrivacyDto 변환 메서드 사용
                        .collect(Collectors.toList()))
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("특정 시간표를 성공적으로 조회하였습니다.")
                        .data(dto)
                        .version("v1.1.4")
                        .build());
    }


    /**
     * 시간표에 강의 추가
     */
    @Transactional
    public ResponseEntity<ResponseDto.Success> addCourseToTimetable(Long timetableId, CourseAssignmentDto requestDto, Principal principal) {
        Timetable timetable = validateProfileOwnership(timetableId, principal);
        Course course = courseRepository.findById(requestDto.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "해당 강의를 찾을 수 없습니다."));

        timetable.getCourses().add(course);
        course.getTimetables().add(timetable);

        timetableRepository.save(timetable);
        courseRepository.save(course);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("강의가 시간표에 추가되었습니다.")
                        .version("v1.1.4")
                        .build());
    }

    /**
     * 시간표에서 강의 제거
     */
    @Transactional
    public ResponseEntity<ResponseDto.Success> removeCourseFromTimetable(Long timetableId, Long courseId, Principal principal) {
        Timetable timetable = validateProfileOwnership(timetableId, principal);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "해당 강의를 찾을 수 없습니다."));

        timetable.getCourses().remove(course);
        course.getTimetables().remove(timetable);

        timetableRepository.save(timetable);
        courseRepository.save(course);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("강의가 시간표에서 제거되었습니다.")
                        .version("v1.1.4")
                        .build());
    }

    /** 🟢 시간표에 개인 일정 추가 (중복은 방지되도록 코드 수정 완) */
    @Transactional
    public ResponseEntity<ResponseDto.Success> addPrivacyToTimetable(
            Long timetableId, PrivacyAssignmentDto requestDto, Principal principal) {

        Timetable timetable = validateProfileOwnership(timetableId, principal);
        Privacy privacy = privacyRepository.findById(requestDto.getPrivacyId())
                .orElseThrow(() -> new AppException(ErrorCode.PRIVACY_NOT_FOUND, "해당 개인 일정을 찾을 수 없습니다."));

        if (timetable.getPrivacies().contains(privacy)) {
            throw new AppException(ErrorCode.DUPLICATE_ENTRY, "해당 개인 일정은 이미 추가되어 있습니다.");
        }

        // 시간표에 개인 일정 추가
        timetable.getPrivacies().add(privacy);
        privacy.getTimetables().add(timetable);

        timetableRepository.save(timetable);
        privacyRepository.save(privacy);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("개인 일정이 시간표에 추가되었습니다.")
                        .version("v1.1.4")
                        .build());
    }


    /**
     * 시간표에서 개인 일정 제거
     */
    @Transactional
    public ResponseEntity<ResponseDto.Success> removePrivacyFromTimetable(Long timetableId, Long privacyId, Principal principal) {
        Timetable timetable = validateProfileOwnership(timetableId, principal);

        Privacy privacy = privacyRepository.findById(privacyId)
                .orElseThrow(() -> new AppException(ErrorCode.TIMETABLE_NOT_FOUND, "해당 개인 일정을 찾을 수 없습니다."));

        timetable.getPrivacies().remove(privacy);
        privacy.getTimetables().remove(timetable);

        timetableRepository.save(timetable);
        privacyRepository.save(privacy);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("시간표에서 개인 일정이 성공적으로 제거되었습니다.")
                        .version("v1.1.4")
                        .build());
    }
    @Transactional
    public ResponseEntity<ResponseDto.Success> updateTimetableName(Long timetableId, TimetableDto timetableDto, Principal principal) {
        Timetable timetable = validateProfileOwnership(timetableId, principal);

        // 이름 변경
        timetable.setName(timetableDto.getName());
        timetableRepository.save(timetable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("시간표 이름이 성공적으로 변경되었습니다.")
                        .data(new TimetableDto(timetable.getId(), timetable.getName()))
                        .version("v1.1.4")
                        .build());
    }

    /**
     * Helper Method: Principal을 이용해 Profile 조회
     */
    private Profile getProfileByPrincipal(Principal principal) {
        return profileRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    /**
     * Helper Method: 주어진 시간표 ID가 현재 로그인된 사용자의 것인지 검증
     */
    private Timetable validateProfileOwnership(Long timetableId, Principal principal) {
        return timetableRepository.findById(timetableId)
                .filter(timetable -> timetable.getProfile().getEmail().equals(principal.getName()))
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN, "접근 권한이 없습니다."));
    }
}
