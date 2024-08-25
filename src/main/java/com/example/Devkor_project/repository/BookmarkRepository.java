package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Bookmark;
import com.example.Devkor_project.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>
{
    @Query(value = "SELECT * FROM bookmark WHERE profile_id = :profile_id AND course_id = :course_id", nativeQuery = true)
    List<Bookmark> searchBookmark(@Param("profile_id") Long profile_id, @Param("course_id") Long course_id);

    @Query(value = "SELECT * FROM bookmark WHERE profile_id = :profile_id ORDER BY bookmark_id DESC",
            countQuery = "SELECT * FROM bookmark WHERE profile_id = :profile_id",
            nativeQuery = true)
    Page<Bookmark> findByProfileId(@Param("profile_id") Long profile_id, Pageable pageable);
}
