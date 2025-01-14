package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Privacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivacyRepository extends JpaRepository<Privacy, Long> {

    // timetables 컬렉션에 포함된 timetable의 ID를 기준으로 쿼리 수정
    List<Privacy> findByTimetables_Id(Long timetableId);}
