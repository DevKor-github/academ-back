package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.PointDto;
import com.example.Devkor_project.dto.ProfileDto;
import com.example.Devkor_project.entity.Code;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class PointService {

    @Autowired ProfileRepository profileRepository;

    /* 강의평 열람 권한 구매 서비스 */
    @Transactional
    public LocalDate buyAccessAuthority(Principal principal, PointDto.Buy dto)
    {
        // 강의평 열람 권한 구매 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 강의평 열람 권한 구매 요청을 보낸 사용자의 계정이 존재하지 않으면 예외 처리
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        int day = 0;                            // 추가할 날짜 수
        int currentPoint = profile.getPoint();  // 현재 사용자의 포인트

        // 사용자의 포인트가 충분한지 확인
        if(Objects.equals(dto.getItem(), "30DAYS")) {

            if(profile.getPoint() < 100)
                throw new AppException(ErrorCode.NOT_ENOUGH_POINT, dto.getItem());

            day = 30;
            profile.setPoint(currentPoint - 100);
        }
        else if(Objects.equals(dto.getItem(), "90DAYS")) {

            if(profile.getPoint() < 200)
                throw new AppException(ErrorCode.NOT_ENOUGH_POINT, dto.getItem());

            day = 90;
            profile.setPoint(currentPoint - 200);
        }
        else if(Objects.equals(dto.getItem(), "180DAYS")) {

            if(profile.getPoint() < 400)
                throw new AppException(ErrorCode.NOT_ENOUGH_POINT, dto.getItem());

            day = 180;
            profile.setPoint(currentPoint - 400);
        }
        else
            throw new AppException(ErrorCode.INVALID_ITEM, dto.getItem());

        // 현재 날짜와 사용자의 열람권 구매 전의 강의평 열람 권한 만료 날짜를 비교
        LocalDate finalExpirationDate = null;

        if(LocalDate.now().isAfter(profile.getAccess_expiration_date())) {
            finalExpirationDate = LocalDate.now().plusDays(day);
        }
        else {
            finalExpirationDate = profile.getAccess_expiration_date().plusDays(day);
        }

        profile.setAccess_expiration_date(finalExpirationDate);
        profileRepository.save(profile);

        return finalExpirationDate;
    }
}
