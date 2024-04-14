package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.CourseDto;
import com.example.Devkor_project.dto.SearchCourseRequestDto;
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
    public List<CourseDto> searchCourse(SearchCourseRequestDto dto)
    {
        // 검색어가 2글자 미만이면 예외 발생
        if(dto.getSearchWord().length() < 2)
            throw new AppException(ErrorCode.SHORT_SEARCH_WORD, "검색어는 2글자 이상이어야 합니다.");

        List<CourseDto> courses;

        switch (dto.getSearchMode())
        {
            // 전체 검색
            case "ALL":
                courses = courseRepository.searchCourseByAll(dto.getSearchWord())
                        .stream()
                        .map(CourseDto::CourseToCousreDto)
                        .collect(Collectors.toList());
                break;
            // 강의명 검색
            case "COURSE_NAME":
                courses = courseRepository.searchCourseByName(dto.getSearchWord())
                        .stream()
                        .map(CourseDto::CourseToCousreDto)
                        .collect(Collectors.toList());
                break;
            // 교수명 검색
            case "PROFESSOR":
                courses = courseRepository.searchCourseByProfessor(dto.getSearchWord())
                        .stream()
                        .map(CourseDto::CourseToCousreDto)
                        .collect(Collectors.toList());
                break;
            // 학수번호 검색
            case "COURSE_CODE":
                courses = courseRepository.searchCourseByCourseCode(dto.getSearchWord())
                        .stream()
                        .map(CourseDto::CourseToCousreDto)
                        .collect(Collectors.toList());
                break;
            // searchMode에 잘못된 값이 들어오면 예외 발생
            default:
                throw new AppException(ErrorCode.UNEXPECTED_ERROR, "예기치 못한 오류가 발생하였습니다.");
        }

        // 검색 결과가 없다면 예외 발생
        if(courses.isEmpty())
            throw new AppException(ErrorCode.NO_RESULT, "검색 결과가 없습니다.");

        return courses;
    }
}
