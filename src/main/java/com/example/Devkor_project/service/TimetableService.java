package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.PrivacyDto;
import com.example.Devkor_project.dto.ResponseDto;
import com.example.Devkor_project.dto.TimetableDto;
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

    /** 🟢 로그인된 사용자의 시간표 생성 */
    @Transactional
    public Timetable createTimetableForProfile(TimetableDto timetableDto, Principal principal) {
        // Principal에서 사용자 이메일 추출
        String email = principal.getName();

        // 이메일로 Profile 조회
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // Timetable 생성
        Timetable timetable = Timetable.builder()
                .profile(profile)  // Profile 연관
                .name(timetableDto.getName())  // 요청 데이터에서 이름 설정
                .build();

        return timetableRepository.save(timetable);
    }


    /** 🟢 시간표에 강의 추가 */
    @Transactional
    public ResponseEntity<?> addCourseToTimetable(Long timetableId, Long courseId, Principal principal) {
        Timetable timetable = validateProfileOwnership(timetableId, principal);
        Course course = courseRepository.findById(courseId).orElse(null);

        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDto.Error.builder()
                            .code("NOT_FOUND")
                            .message("해당 강의를 찾을 수 없습니다.")
                            .version("v1.1.4")
                            .build());
        }

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

    /** 🟢 시간표에서 강의 제거 */
    @Transactional
    public ResponseEntity<?> removeCourseFromTimetable(Long timetableId, Long courseId, Principal principal) {
        Timetable timetable = validateProfileOwnership(timetableId, principal);
        Course course = courseRepository.findById(courseId).orElse(null);

        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDto.Error.builder()
                            .code("NOT_FOUND")
                            .message("해당 강의를 찾을 수 없습니다.")
                            .version("v1.1.4")
                            .build());
        }

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

    /** 🟢 시간표에 개인 일정 추가 */
    @Transactional
    public ResponseEntity<ResponseDto.Success> addPrivacyToTimetable(Long timetableId, PrivacyDto privacyDto, Principal principal) {
        Timetable timetable = validateProfileOwnership(timetableId, principal);

        Privacy privacy = Privacy.builder()
                .name(privacyDto.getName())
                .day(privacyDto.getDay())
                .startTime(privacyDto.getStartTime())
                .finishTime(privacyDto.getFinishTime())
                .location(privacyDto.getLocation())
                .build();

        privacy.getTimetables().add(timetable);
        privacyRepository.save(privacy);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("개인 일정이 시간표에 추가되었습니다.")
                        .data(privacy)
                        .version("v1.1.4")
                        .build());
    }

    /** 🔹 Helper Method: Principal을 이용해 Profile 조회 */
    private Profile getProfileByPrincipal(Principal principal) {
        return profileRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    /** 🔹 Helper Method: 주어진 시간표 ID가 현재 로그인된 사용자의 것인지 검증 */
    private Timetable validateProfileOwnership(Long timetableId, Principal principal) {
        return timetableRepository.findById(timetableId)
                .filter(timetable -> timetable.getProfile().getEmail().equals(principal.getName()))
                .orElseThrow(() -> new RuntimeException("접근 권한이 없습니다."));
    }
}
