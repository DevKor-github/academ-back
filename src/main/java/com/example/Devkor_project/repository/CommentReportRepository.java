package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.CommentReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentReportRepository extends JpaRepository<CommentReport, Long>
{
    // 모든 CommentReport 개수
    @Query(value = "SELECT count(*) FROM comment_report", nativeQuery = true)
    Long countAllReports();

    // 페이지 개수 만큼의 CommentReport
    @Query(value = "SELECT * FROM comment_report ORDER BY created_at DESC, comment_report_id DESC",
            countQuery = "SELECT count(*) FROM comment_report",
            nativeQuery = true)
    Page<CommentReport> reportsByPage(Pageable pageable);

    // profile_id, comment_id로 CommentReport 조회
    @Query(value = "SELECT * FROM comment_report WHERE profile_id = :profile_id AND comment_id = :comment_id", nativeQuery = true)
    List<CommentReport> searchCommentReport(@Param("profile_id") Long profile_id, @Param("comment_id") Long comment_id);
}
