package com.example.Devkor_project.service;

import com.example.Devkor_project.entity.Bookmark;
import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.BookmarkRepository;
import com.example.Devkor_project.repository.CourseRepository;
import com.example.Devkor_project.repository.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

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

    /* 강의 검색 서비스 */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> searchCourse(String keyword)
    {
        // 검색어가 2글자 미만이면 예외 발생
        if(keyword.length() < 2)
            throw new AppException(ErrorCode.SHORT_SEARCH_WORD, keyword);

        // 강의명 + 교수명 + 학수번호 검색
        List<Course> courses = courseRepository.searchCourse(keyword);

        // 검색 결과가 없다면 예외 발생
        if(courses.isEmpty())
            throw new AppException(ErrorCode.NO_RESULT, keyword);

        // entity를 HashMap으로 변경
        List<Map<String, Object>> processedCourses = new ArrayList<>();;
        for(int i = 0; i < courses.size(); i++)
        {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> courseMap = objectMapper.convertValue(courses.get(i), Map.class);
            Map<String, Object> courseProcessedMap = new HashMap<String, Object>();

            for (Map.Entry<String, Object> entry : courseMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                courseProcessedMap.put(key, value);
            }

            processedCourses.add(courseProcessedMap);
        }

        return processedCourses;
    }

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
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, course_id));


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
