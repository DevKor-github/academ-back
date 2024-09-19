package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Bookmark;
import com.example.Devkor_project.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long>
{
    // profile_id, comment_id로 CommentLike 조회
    @Query(value = "SELECT * FROM comment_like WHERE profile_id = :profile_id AND comment_id = :comment_id", nativeQuery = true)
    CommentLike searchCommentLike(@Param("profile_id") Long profile_id, @Param("comment_id") Long comment_id);
}
