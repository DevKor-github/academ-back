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
}
