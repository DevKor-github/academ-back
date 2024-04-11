package com.example.Devkor_project.service;

import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CheckAuthorityService
{
    // 허용된 권한을 가지고 있는지 체크하는 메서드
    public void checkAuthority(HttpServletRequest request, String authority)
    {
        HttpSession session = request.getSession(false);
        if(session == null)
            throw new AppException(ErrorCode.NO_AUTHORITY, "권한이 없습니다.");
        else if(Objects.equals(authority, "ADMIN") &&
                !Objects.equals(session.getAttribute("role").toString(), "ADMIN"))
            throw new AppException(ErrorCode.NO_AUTHORITY, "ADMIN 권한이 필요합니다.");
        else if(Objects.equals(authority, "USER") &&
                !Objects.equals(session.getAttribute("role").toString(), "ADMIN") &&
                !Objects.equals(session.getAttribute("role").toString(), "USER"))
            throw new AppException(ErrorCode.NO_AUTHORITY, "USER 권한이 필요합니다.");
    }
}
