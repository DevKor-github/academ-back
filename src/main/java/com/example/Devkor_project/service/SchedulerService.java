package com.example.Devkor_project.service;

import com.example.Devkor_project.repository.CodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/*
    code 데이터베이스의 오래된 인증번호 정보를 자동으로 삭제하는 scheduler
    매일 오후 12시에 자동으로 하루가 지난 인증번호 정보를 데이터베이스에서 삭제합니다.
*/

@Service
@RequiredArgsConstructor
public class SchedulerService
{
    @Autowired
    private CodeRepository codeRepository;


    @Transactional
    @Async
    @Scheduled(cron = "0 0 12 * * *")
    public void autoDeleteCode()
    {
        codeRepository.deleteOldCode(LocalDate.now().minusDays(1));
    }
}
