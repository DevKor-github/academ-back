package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Bookmark;
import com.example.Devkor_project.entity.Comment;
import com.example.Devkor_project.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>
{
    @Query(value = "SELECT * FROM comment WHERE profile_id = :profile_id AND course_id = :course_id", nativeQuery = true)
    List<Comment> searchComment(@Param("profile_id") Long profile_id, @Param("course_id") Long course_id);

    @Query(value = "SELECT * FROM comment WHERE course_id = :course_id", nativeQuery = true)
    List<Comment> findByCourseId(@Param("course_id") Long course_id);
}
