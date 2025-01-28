package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Privacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivacyRepository extends JpaRepository<Privacy, Long> {

    // timetables 컬렉션에 포함된 timetable의 ID를 기준으로 쿼리 수정
    List<Privacy> findByTimetables_Id(Long timetableId);
    // timetableIds 리스트와 연결된 모든 개인일정을 조회
    List<Privacy> findByTimetables_IdIn(List<Long> timetableIds);


    // 특정 이름을 가진 개인일정을 조회 (예시로 추가)
    List<Privacy> findByName(String name);

}

