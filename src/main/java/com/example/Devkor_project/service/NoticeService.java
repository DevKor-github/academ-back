package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.NoticeDto;
import com.example.Devkor_project.entity.Notice;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService
{
    @Autowired NoticeRepository noticeRepository;

    /* 공지사항 목록 조회 서비스 */
    public List<NoticeDto.List> noticeList(int page)
    {
        // Pageable 객체 생성 (size = 10개)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Notice> notices = noticeRepository.noticesByPage(pageable);

        if(notices.isEmpty())
            throw new AppException(ErrorCode.NO_NOTICE, null);

        List<NoticeDto.List> noticeDtos = notices.stream()
                .map(notice -> {
                    return NoticeDto.List.builder()
                            .notice_id(notice.getNotice_id())
                            .title(notice.getTitle())
                            .created_at(notice.getCreated_at())
                            .build();
                })
                .toList();

        return noticeDtos;
    }

    /* 공지사항 상세 조회 서비스 */
    public NoticeDto.Detail noticeDetail(Long notice_id)
    {
        // 해당 건의사항이 존재하는지 확인
        Notice notice = noticeRepository.findById(notice_id)
                .orElseThrow(() -> new AppException(ErrorCode.NOTICE_NOT_FOUND, notice_id));

        return NoticeDto.Detail.builder()
                .notice_id(notice.getNotice_id())
                .title(notice.getTitle())
                .detail(notice.getDetail())
                .created_at(notice.getCreated_at())
                .build();
    }

    /* 공지사항 개수 서비스 */
    public Long countNotice()
    {
        return noticeRepository.countAllNotices();
    }
}
