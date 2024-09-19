package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.CommentReport;
import com.example.Devkor_project.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
