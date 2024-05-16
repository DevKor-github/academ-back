package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.CourseDto;
import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.CourseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HomeService
{

    @Autowired
    CourseRepository courseRepository;

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

        List<Map<String, Object>> processedCourses = new ArrayList<>();;
        for(int i = 0; i < courses.size(); i++)
        {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> processedCourse = objectMapper.convertValue(courses.get(i), Map.class);
            processedCourses.add(processedCourse);
        }

        return processedCourses;
    }
}
