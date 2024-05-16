package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long>
{
    @Query(value = "SELECT * FROM course WHERE name LIKE %:word% OR professor LIKE %:word% OR course_code LIKE %:word%", nativeQuery = true)
    List<Course> searchCourse(@Param("word") String word);

    @Query(value = "SELECT count(*) FROM course", nativeQuery = true)
    Long countCourse();
}
