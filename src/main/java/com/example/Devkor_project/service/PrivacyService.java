package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.PrivacyDto;
import com.example.Devkor_project.entity.Privacy;
import com.example.Devkor_project.entity.Timetable;
import com.example.Devkor_project.repository.PrivacyRepository;
import com.example.Devkor_project.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivacyService {

    private final PrivacyRepository privacyRepository;
    private final TimetableRepository timetableRepository;

    @Transactional
    public List<PrivacyDto> getAllPrivacy(Long timetableId) {
        return privacyRepository.findByTimetables_Id(timetableId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PrivacyDto createPrivacy(PrivacyDto privacyDto) {
        // 해당하는 모든 Timetable 가져오기
        List<Timetable> timetables = timetableRepository.findAllById(privacyDto.getTimetableIds());

        Privacy privacy = Privacy.builder()
                .timetables(timetables)
                .name(privacyDto.getName())
                .day(privacyDto.getDay())
                .startTime(privacyDto.getStartTime())
                .finishTime(privacyDto.getFinishTime())
                .location(privacyDto.getLocation())
                .build();

        Privacy savedPrivacy = privacyRepository.save(privacy);
        return convertToDto(savedPrivacy);
    }

    @Transactional
    public PrivacyDto updatePrivacy(Long id, PrivacyDto privacyDto) {
        Privacy privacy = privacyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Privacy not found"));

        List<Timetable> timetables = timetableRepository.findAllById(privacyDto.getTimetableIds());
        privacy.setName(privacyDto.getName());
        privacy.setDay(privacyDto.getDay());
        privacy.setStartTime(privacyDto.getStartTime());
        privacy.setFinishTime(privacyDto.getFinishTime());
        privacy.setLocation(privacyDto.getLocation());

        Privacy updatedPrivacy = privacyRepository.save(privacy);
        return convertToDto(updatedPrivacy);
    }

    @Transactional
    public void deletePrivacy(Long id) {
        privacyRepository.deleteById(id);
    }

    @Transactional
    public PrivacyDto addPrivacyToTimetable(Long timetableId, PrivacyDto privacyDto) {
        // timetableId로 Timetable을 조회
        Timetable timetable = timetableRepository.findById(timetableId)
                .orElseThrow(() -> new RuntimeException("Timetable not found"));

        // Privacy 객체 생성
        Privacy privacy = Privacy.builder()
                .name(privacyDto.getName())
                .day(privacyDto.getDay())
                .startTime(privacyDto.getStartTime())
                .finishTime(privacyDto.getFinishTime())
                .location(privacyDto.getLocation())
                .build();

        // Privacy 객체에 Timetable 추가
        privacy.getTimetables().add(timetable);

        // Privacy 저장
        Privacy savedPrivacy = privacyRepository.save(privacy);

        // PrivacyDto로 변환하여 반환
        return convertToDto(savedPrivacy);
    }

    @Transactional
    public void removePrivacyFromTimetable(Long timetableId, Long privacyId) {
        // Privacy 조회
        Privacy privacy = privacyRepository.findById(privacyId)
                .orElseThrow(() -> new RuntimeException("Privacy not found"));

        // 특정 Timetable과 Privacy의 관계를 끊기
        privacy.getTimetables().removeIf(timetable -> timetable.getId().equals(timetableId));

        // 변경 사항 저장
        privacyRepository.save(privacy);
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
