package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Bookmark;
import com.example.Devkor_project.entity.Comment;
import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>
{
    @Query(value = "SELECT * FROM comment WHERE profile_id = :profile_id AND course_id = :course_id", nativeQuery = true)
    List<Comment> searchComment(@Param("profile_id") Long profile_id, @Param("course_id") Long course_id);

    @Query(value = "SELECT * FROM comment WHERE profile_id = :profile_id", nativeQuery = true)
    List<Comment> findAllByProfileId(@Param("profile_id") Long profile_id);

    @Query(value = "SELECT count(*) FROM comment WHERE profile_id = :profile_id", nativeQuery = true)
    int countCommentByProfileId(@Param("profile_id") Long profile_id);

    @Query(value = "SELECT * FROM comment WHERE course_id = :course_id ORDER BY created_at DESC",
            countQuery = "SELECT * FROM comment WHERE course_id = :course_id",
            nativeQuery = true)
    Page<Comment> findByCourseIdOrderNewest(@Param("course_id") Long course_id, Pageable pageable);

    @Query(value = "SELECT comment.* FROM comment NATURAL JOIN comment_rating WHERE course_id = :course_id ORDER BY rating DESC",
            countQuery = "SELECT * FROM comment WHERE course_id = :course_id",
            nativeQuery = true)
    Page<Comment> findByCourseIdOrderRatingDesc(@Param("course_id") Long course_id, Pageable pageable);

    @Query(value = "SELECT comment.* FROM comment NATURAL JOIN comment_rating WHERE course_id = :course_id ORDER BY rating ASC",
            countQuery = "SELECT * FROM comment WHERE course_id = :course_id",
            nativeQuery = true)
    Page<Comment> findByCourseIdOrderRatingAsc(@Param("course_id") Long course_id, Pageable pageable);

    @Query(value = "SELECT * FROM comment WHERE course_id = :course_id ORDER BY likes DESC",
            countQuery = "SELECT * FROM comment WHERE course_id = :course_id",
            nativeQuery = true)
    Page<Comment> findByCourseIdOrderLikesDesc(@Param("course_id") Long course_id, Pageable pageable);

    @Query(value = "SELECT * FROM comment WHERE course_id = :course_id ORDER BY likes ASC",
            countQuery = "SELECT * FROM comment WHERE course_id = :course_id",
            nativeQuery = true)
    Page<Comment> findByCourseIdOrderLikesAsc(@Param("course_id") Long course_id, Pageable pageable);

    @Query(value = "SELECT * FROM comment WHERE profile_id = :profile_id ORDER BY created_at DESC",
            countQuery = "SELECT * FROM comment WHERE profile_id = :profile_id",
            nativeQuery = true)
    Page<Comment> findByProfileIdOrderNewest(@Param("profile_id") Long profile_id, Pageable pageable);

}
