package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>
{
    @Query(value = "SELECT * FROM course WHERE name LIKE %:word% OR professor LIKE %:word% OR course_code LIKE %:word% ORDER BY year DESC", nativeQuery = true)
    List<Course> searchCourseByNewest(@Param("word") String word);

    @Query(value = "SELECT course.* FROM course NATURAL JOIN course_rating WHERE name LIKE %:word% OR professor LIKE %:word% OR course_code LIKE %:word% ORDER BY avg_rating DESC", nativeQuery = true)
    List<Course> searchCourseByRatingDesc(@Param("word") String word);

    @Query(value = "SELECT course.* FROM course NATURAL JOIN course_rating WHERE name LIKE %:word% OR professor LIKE %:word% OR course_code LIKE %:word% ORDER BY avg_rating ASC", nativeQuery = true)
    List<Course> searchCourseByRatingAsc(@Param("word") String word);

    @Query(value = "SELECT count(*) FROM course", nativeQuery = true)
    Long countCourse();
}
