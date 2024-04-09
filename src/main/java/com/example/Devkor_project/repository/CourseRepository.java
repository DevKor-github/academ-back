package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long>
{
}
