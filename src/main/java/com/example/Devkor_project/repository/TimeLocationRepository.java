package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.entity.TimeLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeLocationRepository extends JpaRepository<TimeLocation, Long>
{
    @Query(value = "SELECT * FROM time_location WHERE course_id = :course_id", nativeQuery = true)
    List<TimeLocation> findByCourseId(Long course_id);

    @Query(value = "SELECT tl.* FROM time_location tl " +
            "JOIN course c ON tl.course_id = c.course_id " +
            "JOIN timetable_courses tc ON c.course_id = tc.course_id " +
            "WHERE tc.timetable_id = :timetable_id", nativeQuery = true)
    List<TimeLocation> findByTimetableId(@Param("timetable_id") Long timetableId);

    // 📌 여러 개의 course_id를 한 번에 조회하는 메서드 추가
    @Query("SELECT tl FROM TimeLocation tl WHERE tl.course_id.course_id IN :courseIds")
    List<TimeLocation> findByCourseIds(@Param("courseIds") List<Long> courseIds);
}
