package com.example.Devkor_project.service;

import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class AdminService
{
    @Autowired
    CourseRepository courseRepository;

    /* 대학원 강의 데이터베이스 초기화 서비스 */
    @Transactional
    @SuppressWarnings("unchecked")  // Object가 Map 형식이 아닐 수도 있다는 경고 무시
    public void initCourseDatabase(Map<String,Object> data)
    {
        // 데이터베이스 데이터 삭제
        courseRepository.deleteAll();

        // 데이터베이스에 데이터 추가
        Long idIndex = 0L;

        for(String graduateSchool : data.keySet())
        {
            Map<String,Object> value1 = (Map<String,Object>)data.get(graduateSchool);
            for(String department : value1.keySet())
            {
                Map<String,Object> dataArray = (Map<String,Object>)value1.get(department);
                List<Map<String, String>> dataList = (List<Map<String, String>>) dataArray.get("data");

                for(Map<String, String> course : dataList)
                {
                    String credit = null;
                    String time_location = null;

                    if (!course.get("time").isEmpty())
                        credit = course.get("time").replaceAll("\\(.*?\\)", "");

                    if (!course.get("time_room").isEmpty())
                        time_location = course.get("time_room").replaceAll("<.*?>", "");

                    Course information = Course.builder()
                            .course_id(idIndex)
                            .course_code(course.get("cour_cd"))
                            .graduate_school(graduateSchool)
                            .department(department)
                            .year(course.get("year"))
                            .semester(course.get("term"))
                            .name(course.get("cour_nm"))
                            .professor(course.get("prof_nm"))
                            .credit(credit)
                            .time_location(time_location)
                            .rating(-1)
                            .amount_of_studying(-1)
                            .difficulty(-1)
                            .delivery_power(-1)
                            .grading(-1)
                            .build();

                    courseRepository.save(information);

                    idIndex++;
                }
            }
        }
    }

    /* 대학원 강의 데이터베이스 추가 서비스 */
    @Transactional
    @SuppressWarnings("unchecked")  // Object가 Map 형식이 아닐 수도 있다는 경고 무시
    public void insertCourseDatabase(Map<String,Object> data)
    {
        // 데이터베이스에 데이터 추가
        Long idIndex = courseRepository.countCourse();

        for(String graduateSchool : data.keySet())
        {
            Map<String,Object> value1 = (Map<String,Object>)data.get(graduateSchool);
            for(String department : value1.keySet())
            {
                Map<String,Object> dataArray = (Map<String,Object>)value1.get(department);
                List<Map<String, String>> dataList = (List<Map<String, String>>) dataArray.get("data");

                for(Map<String, String> course : dataList)
                {
                    String credit = null;
                    String time_location = null;

                    if (!course.get("time").isEmpty())
                        credit = course.get("time").replaceAll("\\(.*?\\)", "");

                    if (!course.get("time_room").isEmpty())
                        time_location = course.get("time_room").replaceAll("<.*?>", "");

                    Course information = Course.builder()
                            .course_id(idIndex)
                            .course_code(course.get("cour_cd"))
                            .graduate_school(graduateSchool)
                            .department(department)
                            .year(course.get("year"))
                            .semester(course.get("term"))
                            .name(course.get("cour_nm"))
                            .professor(course.get("prof_nm"))
                            .credit(credit)
                            .time_location(time_location)
                            .rating(-1)
                            .amount_of_studying(-1)
                            .difficulty(-1)
                            .delivery_power(-1)
                            .grading(-1)
                            .build();

                    courseRepository.save(information);

                    idIndex++;
                }
            }
        }
    }
}
