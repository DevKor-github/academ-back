package com.example.Devkor_project.service;

import com.example.Devkor_project.entity.Bookmark;
import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.BookmarkRepository;
import com.example.Devkor_project.repository.CourseRepository;
import com.example.Devkor_project.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@Slf4j
public class CourseService
{
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    BookmarkRepository bookmarkRepository;

    /* 강의 북마크 서비스 */
    public void bookmark(Principal principal, Long course_id)
    {
        // 북마크 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 북마크 요청을 보낸 사용자의 계정 Profile entity
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));

        // 사용자가 북마크할 강의
        Course course = courseRepository.findById(course_id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        // Bookmark entity 생성
        Bookmark bookmark = Bookmark.builder()
                .profile_id(profile)
                .course_id(course)
                .build();

        // 데이터베이스에 저장
        bookmarkRepository.save(bookmark);
    }
}
