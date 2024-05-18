package com.example.Devkor_project.service;

import com.example.Devkor_project.entity.Bookmark;
import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.BookmarkRepository;
import com.example.Devkor_project.repository.CourseRepository;
import com.example.Devkor_project.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
    @Transactional
    public void bookmark(Principal principal, Long course_id)
    {
        // 북마크 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 북마크 요청을 보낸 사용자의 계정이 존재하지 않으면 예외 처리
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // 사용자가 북마크할 강의가 존재하지 않으면 예외 처리
        Course course = courseRepository.findById(course_id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, course_id.toString()));


        List<Bookmark> bookmark = bookmarkRepository.searchBookmark(profile.getProfile_id(), course.getCourse_id());
        // 해당 북마크가 정보가 현재 존재하지 않으면, 북마크 생성
        if(bookmark.isEmpty())
        {
            // Bookmark entity 생성
            Bookmark newBookmark = Bookmark.builder()
                    .profile_id(profile)
                    .course_id(course)
                    .build();

            bookmarkRepository.save(newBookmark);
        }
        // 해당 북마크가 정보가 이미 존재하면, 북마크 해제
        else
            bookmarkRepository.delete(bookmark.getFirst());

    }
}
