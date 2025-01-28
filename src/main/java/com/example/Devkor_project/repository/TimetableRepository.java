package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Timetable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    // 특정 Profile의 모든 시간표 검색
    @Query("SELECT t FROM Timetable t WHERE t.profile.profile_id = :profileId")
    List<Timetable> findByProfile_profileId(@Param("profileId") Long profileId);

    // 그 프로필 가진 사람의 특정 시간표(Timetable ID) 검색
    @Query("SELECT t FROM Timetable t WHERE t.id = :timetableId AND t.profile.profile_id = :profileId")
    Timetable findByIdAndProfile_profileId(@Param("timetableId") Long timetableId, @Param("profileId") Long profileId);

    // Profile 이메일을 기반으로 모든 시간표 조회
    List<Timetable> findAllByProfile_Email(String email);

    @Query("SELECT t FROM Timetable t WHERE t.profile.email = :email")
    List<Timetable> findTimetablesByEmail(@Param("email") String email);


}
