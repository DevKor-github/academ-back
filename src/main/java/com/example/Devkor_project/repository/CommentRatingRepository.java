package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.CommentRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRatingRepository extends JpaRepository<CommentRating, Long> {
}
