package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.*;
import com.example.Devkor_project.entity.*;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.repository.CourseRepository;
import com.example.Devkor_project.repository.PrivacyRepository;
import com.example.Devkor_project.repository.ProfileRepository;
import com.example.Devkor_project.repository.TimetableRepository;
import com.example.Devkor_project.repository.TimeLocationRepository;
import com.example.Devkor_project.util.TimeLocationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final TimeLocationRepository timeLocationRepository;
    private final CourseRepository courseRepository;
    private final PrivacyRepository privacyRepository;
    private final ProfileRepository profileRepository;
    private final VersionProvider versionProvider;

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
                        .version(versionProvider.getVersion())
                        .build());
    }

    /**
     * 특정 시간표 조회 (상세 정보 포함)
     */
    @Transactional
    public ResponseEntity<ResponseDto.Success> getTimetableByIdWithDetails(Long timetableId, Principal principal) {
        Timetable timetable = validateProfileOwnership(timetableId, principal);

        // ✅ 시간표에 포함된 강의 조회
        List<Course> courses = timetable.getCourses();

        // ✅ 강의 ID 리스트 추출
        List<Long> courseIds = courses.stream().map(Course::getCourse_id).toList();

        // ✅ 레포지토리에서 TimeLocation 정보 가져오기
        List<TimeLocation> timeLocations = courseIds.isEmpty() ? List.of() : timeLocationRepository.findByCourseIds(courseIds);

        // ✅ 강의 ID별로 TimeLocation 매핑
        Map<Long, List<CourseDto.TimeLocation>> timeLocationMap = timeLocations.stream()
                .collect(Collectors.groupingBy(
                        tl -> tl.getCourse_id().getCourse_id(),
                        Collectors.mapping(
                                tl -> new CourseDto.TimeLocation(
                                        tl.getDay(),
                                        tl.getStartPeriod(),
                                        tl.getEndPeriod(),
                                        tl.getLocation()
                                ),
                                Collectors.toList()
                        )
                ));

        // ✅ DTO 변환 (TimeLocation 포함)
        List<CourseDto.Simple> courseDtos = courses.stream()
                .map(course -> CourseDto.fromCourse(course, timeLocationMap.getOrDefault(course.getCourse_id(), List.of())))
                .collect(Collectors.toList());

        // DTO 변환
        TimetableWithDetailsDto dto = TimetableWithDetailsDto.builder()
                .id(timetable.getId())
                .profileId(timetable.getProfile().getProfile_id())
                .name(timetable.getName())
                .courses(courseDtos)  // ✅ CourseDto 변환 메서드 사용
                .privacies(timetable.getPrivacies().stream()
                        .map(PrivacyDto::fromPrivacy)  // ✅ PrivacyDto 변환 메서드 사용
                        .collect(Collectors.toList()))
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("특정 시간표를 성공적으로 조회하였습니다.")
                        .data(dto)
                        .version(versionProvider.getVersion())
                        .build());
    }


    /**
     * 시간표에 강의 추가
     */
    @Transactional
    public ResponseEntity<ResponseDto.Success> addCourseToTimetable(Long timetableId, Long courseId, Principal principal) {
        Timetable timetable = validateProfileOwnership(timetableId, principal);

        if (courseId == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST, "courseId는 null일 수 없습니다.");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "해당 강의를 찾을 수 없습니다."));

        if (timetable.getCourses() == null) {
            timetable.setCourses(new ArrayList<>()); // 🛠️ Null 체크 후 리스트 초기화
        }
        if (course.getTimetables() == null) {
            course.setTimetables(new ArrayList<>()); // 🛠️ Null 체크 후 리스트 초기화
        }

        if (timetable.getCourses().contains(course)) {
            throw new AppException(ErrorCode.DUPLICATE_ENTRY, "해당 강의는 이미 추가되어 있습니다.");
        }

        // ⏳ 기존 시간표의 강의 및 개인 일정 시간 정보 가져오기
        List<TimeLocationDto> existingTimeLocations = TimeLocationUtil.extractFromTimetable(timetable, timeLocationRepository);

        // 🕒 새 강의의 시간표 정보 가져오기
        List<TimeLocationDto> newCourseTimeLocations = timeLocationRepository.findByCourseIds(List.of(course.getCourse_id()))
                .stream()
                .map(tl -> new TimeLocationDto(tl.getDay(), tl.getStartPeriod(), tl.getEndPeriod(), tl.getLocation()))
                .toList();

        // ⚠️ 기존 일정과 새 강의 시간이 겹치는지 확인
        if (TimeLocationUtil.hasScheduleConflict(existingTimeLocations, newCourseTimeLocations)) {
            throw new AppException(ErrorCode.SCHEDULE_CONFLICT, "시간표에 이미 같은 시간에 다른 강의 또는 개인 일정이 있습니다.");
        }

        // 📌 겹치지 않으면 추가 진행
        timetable.getCourses().add(course);
        course.getTimetables().add(timetable);

        timetableRepository.save(timetable);
        courseRepository.save(course);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("강의가 시간표에 추가되었습니다.")
                        .version(versionProvider.getVersion())
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
                        .version(versionProvider.getVersion())
                        .build());
    }

    /** 🟢 시간표에 개인 일정 추가 (중복은 방지되도록 코드 수정 완) */
    @Transactional
    public ResponseEntity<ResponseDto.Success> addPrivacyToTimetable(Long timetableId, Long privacyId, Principal principal) {
        Timetable timetable = validateProfileOwnership(timetableId, principal);

        if (privacyId == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST, "privacyId는 null일 수 없습니다.");
        }

        Privacy privacy = privacyRepository.findById(privacyId)
                .orElseThrow(() -> new AppException(ErrorCode.PRIVACY_NOT_FOUND, "해당 개인 일정을 찾을 수 없습니다."));

        if (timetable.getPrivacies() == null) {
            timetable.setPrivacies(new ArrayList<>()); // 🛠️ Null 체크 후 리스트 초기화
        }
        if (privacy.getTimetables() == null) {
            privacy.setTimetables(new ArrayList<>()); // 🛠️ Null 체크 후 리스트 초기화
        }

        if (timetable.getPrivacies().contains(privacy)) {
            throw new AppException(ErrorCode.DUPLICATE_ENTRY, "해당 개인 일정은 이미 추가되어 있습니다.");
        }

        // ⏳ 기존 시간표의 강의 및 개인 일정 시간 정보 가져오기
        List<TimeLocationDto> existingTimeLocations = TimeLocationUtil.extractFromTimetable(timetable, timeLocationRepository);

        // 🕒 새 개인 일정의 시간표 정보 생성
        List<TimeLocationDto> newPrivacyTimeLocations = List.of(
                new TimeLocationDto(privacy.getDay(), privacy.getStartTime().getHour(),
                        privacy.getFinishTime().getHour(), privacy.getLocation())
        );

        // ⚠️ 기존 일정과 새 일정 시간이 겹치는지 확인
        if (TimeLocationUtil.hasScheduleConflict(existingTimeLocations, newPrivacyTimeLocations)) {
            throw new AppException(ErrorCode.SCHEDULE_CONFLICT, "시간표에 이미 같은 시간에 다른 강의 또는 개인 일정이 있습니다.");
        }

        // 📌 겹치지 않으면 추가 진행
        timetable.getPrivacies().add(privacy);
        privacy.getTimetables().add(timetable);

        timetableRepository.save(timetable);
        privacyRepository.save(privacy);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("개인 일정이 시간표에 추가되었습니다.")
                        .version(versionProvider.getVersion())
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
                        .version(versionProvider.getVersion())
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
                        .version(versionProvider.getVersion())
                        .build());
    }
    /**
     * 🟢 시간표 삭제
     */
    @Transactional
    public ResponseEntity<ResponseDto.Success> deleteTimetable(Long timetableId, Principal principal) {
        Timetable timetable = validateProfileOwnership(timetableId, principal);

        // 📌 시간표에서 모든 강의 및 개인 일정 관계 해제
        timetable.getCourses().forEach(course -> course.getTimetables().remove(timetable));
        timetable.getPrivacies().forEach(privacy -> privacy.getTimetables().remove(timetable));

        // 📌 시간표 삭제
        timetableRepository.delete(timetable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("시간표가 성공적으로 삭제되었습니다.")
                        .version(versionProvider.getVersion())
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
