package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.PrivacyDto;
import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.entity.Privacy;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.entity.Timetable;
import com.example.Devkor_project.repository.CourseRepository;
import com.example.Devkor_project.repository.PrivacyRepository;
import com.example.Devkor_project.repository.ProfileRepository;
import com.example.Devkor_project.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final CourseRepository courseRepository;
    private final PrivacyRepository privacyRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public Timetable createTimetableForProfile(String username) {
        // username으로 Profile을 찾음
        Profile profile = profileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // 새로운 Timetable 생성
        Timetable timetable = Timetable.builder()
                .profile(profile)  // 프로필과 연결된 시간표 생성
                .build();

        // 시간표 저장
        return timetableRepository.save(timetable);
    }

    @Transactional
    public void addCourseToTimetable(Long timetableId, Long courseId) {
        Timetable timetable = timetableRepository.findById(timetableId)
                .orElseThrow(() -> new RuntimeException("Timetable not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Course와 Timetable 간의 연관 관계 추가
        if (!course.getTimetables().contains(timetable)) {
            course.getTimetables().add(timetable);
        }
        if (!timetable.getCourses().contains(course)) {
            timetable.getCourses().add(course);
        }

        // 변경 사항 저장
        courseRepository.save(course);
        timetableRepository.save(timetable);
    }

    @Transactional
    public void removeCourseFromTimetable(Long timetableId, Long courseId) {
        Timetable timetable = timetableRepository.findById(timetableId)
                .orElseThrow(() -> new RuntimeException("Timetable not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Course와 Timetable 간의 연관 관계 제거
        course.getTimetables().remove(timetable);
        timetable.getCourses().remove(course);

        // 변경 사항 저장
        courseRepository.save(course);
        timetableRepository.save(timetable);
    }

    @Transactional
    public void addPrivacyToTimetable(Long timetableId, PrivacyDto privacyDto) {
        Timetable timetable = timetableRepository.findById(timetableId)
                .orElseThrow(() -> new RuntimeException("Timetable not found"));

        Privacy privacy = Privacy.builder()
                .timetable(timetable)
                .name(privacyDto.getName())
                .day(privacyDto.getDay())
                .startTime(privacyDto.getStartTime())
                .finishTime(privacyDto.getFinishTime())
                .location(privacyDto.getLocation())
                .build();

        privacyRepository.save(privacy);
    }

    @Transactional
    public void removePrivacyFromTimetable(Long timetableId, Long privacyId) {
        Privacy privacy = privacyRepository.findById(privacyId)
                .orElseThrow(() -> new RuntimeException("Privacy not found"));

        // timetableId가 privacy의 timetables 리스트에 포함되어 있는지 검사
        boolean isLinkedToTimetable = privacy.getTimetables().stream()
                .anyMatch(timetable -> timetable.getId().equals(timetableId));

        if (!isLinkedToTimetable) {
            throw new RuntimeException("Privacy does not belong to this Timetable");
        }

        // Timetable과 Privacy 간의 연관 관계 해제
        privacy.getTimetables().removeIf(timetable -> timetable.getId().equals(timetableId));
        privacyRepository.save(privacy);
        }

    // profileId로 timetable 조회
    public List<Timetable> getTimetableByProfileId(Long profileId) {
        List<Timetable> timetables = timetableRepository.findByProfile_profileId(profileId);

        // timetable이 없다면 적절한 메시지를 반환
        if (timetables.isEmpty()) {
            throw new RuntimeException("해당 프로필에 대한 timetable이 없습니다. 새로 만들어보세요!");
        }

        return timetables;
    }
}
