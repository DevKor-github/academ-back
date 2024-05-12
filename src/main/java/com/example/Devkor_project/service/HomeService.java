package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.CourseDto;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HomeService
{

    @Autowired
    CourseRepository courseRepository;

    /*
        강의 검색 서비스
    */
    public List<CourseDto> searchCourse(String keyword)
    {
        // 검색어가 2글자 미만이면 예외 발생
        if(keyword.length() < 2)
            throw new AppException(ErrorCode.SHORT_SEARCH_WORD);

        // 강의명 + 교수명 + 학수번호 검색
        List<CourseDto> courses = courseRepository.searchCourse(keyword)
                .stream()
                .map(CourseDto::CourseToCousreDto)
                .collect(Collectors.toList());

        // 검색 결과가 없다면 예외 발생
        if(courses.isEmpty())
            throw new AppException(ErrorCode.NO_RESULT);

        return courses;
    }
}
