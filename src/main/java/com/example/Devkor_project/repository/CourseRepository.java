package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>
{
    @Query(value = "SELECT * FROM course WHERE name LIKE %:word% OR professor LIKE %:word% OR course_code LIKE %:word% ORDER BY year DESC, semester DESC, course_id DESC",
            countQuery = "SELECT count(*) FROM course WHERE name LIKE %:word% OR professor LIKE %:word% OR course_code LIKE %:word%",
            nativeQuery = true)
    Page<Course> searchCourseByNewest(@Param("word") String word, Pageable pageable);

    @Query(value = "SELECT course.* FROM course NATURAL JOIN course_rating WHERE name LIKE %:word% OR professor LIKE %:word% OR course_code LIKE %:word% ORDER BY avg_rating DESC, course_id DESC",
            countQuery = "SELECT count(*) FROM course NATURAL JOIN course_rating WHERE name LIKE %:word% OR professor LIKE %:word% OR course_code LIKE %:word%",
            nativeQuery = true)
    Page<Course> searchCourseByRatingDesc(@Param("word") String word, Pageable pageable);

    @Query(value = "SELECT course.* FROM course NATURAL JOIN course_rating WHERE name LIKE %:word% OR professor LIKE %:word% OR course_code LIKE %:word% ORDER BY avg_rating ASC, course_id DESC",
            countQuery = "SELECT count(*) FROM course NATURAL JOIN course_rating WHERE name LIKE %:word% OR professor LIKE %:word% OR course_code LIKE %:word%",
            nativeQuery = true)
    Page<Course> searchCourseByRatingAsc(@Param("word") String word, Pageable pageable);

    @Query(value = "SELECT count(*) FROM course", nativeQuery = true)
    Long countCourse();

    @Query(value = "SELECT count(*) FROM course WHERE name LIKE %:word% OR professor LIKE %:word% OR course_code LIKE %:word%", nativeQuery = true)
    int countCourseByKeyword(@Param("word") String word);

    // 크롤링 강의 데이터와 일치하는 Course 조회
    @Query(value = "SELECT * FROM course WHERE course_code = :course_code AND class_number = :class_number AND year = :year AND semester = :semester", nativeQuery = true)
    Course compareWithCrawlingData(
            @Param("course_code") String course_code,
            @Param("class_number") String class_number,
            @Param("year") String year,
            @Param("semester") String semester
    );

    // 해당 연도와 학기의 모든 Course 조회
    @Query(value = "SELECT * FROM course WHERE year = :year AND semester = :semester", nativeQuery = true)
    List<Course> findCourseByYearAndSemester(
            @Param("year") String year,
            @Param("semester") String semester
    );
}
