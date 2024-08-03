package com.example.Devkor_project.repository;

import com.example.Devkor_project.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoticeRepository extends JpaRepository<Notice, Long>
{
    // 모든 Notice 개수
    @Query(value = "SELECT count(*) FROM notice", nativeQuery = true)
    Long countAllNotices();

    // 페이지 개수 만큼의 Notice
    @Query(value = "SELECT * FROM notice ORDER BY created_at DESC, notice_id DESC",
            countQuery = "SELECT count(*) FROM notice",
            nativeQuery = true)
    Page<Notice> noticesByPage(Pageable pageable);
}
