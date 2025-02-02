package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.CourseRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRatingRepository extends JpaRepository<CourseRating, Long> {
}
