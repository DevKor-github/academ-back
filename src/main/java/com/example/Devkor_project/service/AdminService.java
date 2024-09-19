package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.NoticeDto;
import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.entity.CourseRating;
import com.example.Devkor_project.entity.Notice;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.CourseRatingRepository;
import com.example.Devkor_project.repository.CourseRepository;
import com.example.Devkor_project.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class AdminService
{
    @Autowired CourseRepository courseRepository;
    @Autowired CourseRatingRepository courseRatingRepository;
    @Autowired NoticeRepository noticeRepository;

    /* 대학원 강의 데이터베이스 추가 서비스 */
    @Transactional
    @SuppressWarnings("unchecked")  // Object가 Map 형식이 아닐 수도 있다는 경고 무시
    public void insertCourseDatabase(Map<String,Object> data)
    {
        for(String graduateSchool : data.keySet())
        {
            Map<String,Object> value1 = (Map<String,Object>)data.get(graduateSchool);
            for(String department : value1.keySet())
            {
                Map<String,Object> dataArray = (Map<String,Object>)value1.get(department);
                List<Map<String, String>> dataList = (List<Map<String, String>>) dataArray.get("data");

                for(Map<String, String> courseInfo : dataList)
                {
                    String credit = null;
                    String time_location = null;

                    if (!courseInfo.get("time").isEmpty())
                        credit = courseInfo.get("time").replaceAll("\\(.*?\\)", "");

                    if (!courseInfo.get("time_room").isEmpty())
                        time_location = courseInfo.get("time_room").replaceAll("<.*?>", "");

                    CourseRating courseRating = CourseRating.builder()
                            .AVG_rating(0.0)
                            .AVG_r1_amount_of_studying(0.0)
                            .AVG_r2_difficulty(0.0)
                            .AVG_r3_delivery_power(0.0)
                            .AVG_r4_grading(0.0)
                            .COUNT_teach_t1_theory(0)
                            .COUNT_teach_t2_practice(0)
                            .COUNT_teach_t3_seminar(0)
                            .COUNT_teach_t4_discussion(0)
                            .COUNT_teach_t5_presentation(0)
                            .COUNT_learn_t1_theory(0)
                            .COUNT_learn_t2_thesis(0)
                            .COUNT_learn_t3_exam(0)
                            .COUNT_learn_t4_industry(0)
                            .build();

                    courseRatingRepository.save(courseRating);

                    Course course = Course.builder()
                            .courseRating_id(courseRating)
                            .name(courseInfo.get("cour_nm"))
                            .course_code(courseInfo.get("cour_cd"))
                            .professor(courseInfo.get("prof_nm"))
                            .graduate_school(graduateSchool)
                            .department(department)
                            .year(courseInfo.get("year"))
                            .semester(courseInfo.get("term"))
                            .credit(credit)
                            .time_location(time_location)
                            .COUNT_comments(0)
                            .build();

                    courseRepository.save(course);
                }
            }
        }
    }

    /* 공지사항 추가 서비스 */
    public void insertNotice(NoticeDto.Insert dto)
    {
        // 공지사항 엔티티 생성
        Notice notice = Notice.builder()
                .title(dto.getTitle())
                .detail(dto.getDetail())
                .created_at(LocalDate.now())
                .image_1(dto.getImage_1())
                .image_2(dto.getImage_2())
                .image_3(dto.getImage_3())
                .image_4(dto.getImage_4())
                .image_5(dto.getImage_5())
                .build();

        // 공지사항 저장
        noticeRepository.save(notice);
    }

    /* 공지사항 수정 서비스 */
    public void updateNotice(NoticeDto.Update dto)
    {
        // 해당 공지사항이 존재하는지 확인
        Notice notice = noticeRepository.findById(dto.getNotice_id())
            .orElseThrow(() -> new AppException(ErrorCode.NOTICE_NOT_FOUND, dto.getNotice_id()));

        // 변경사항 적용
        notice.setTitle(dto.getTitle());
        notice.setDetail(dto.getDetail());
        notice.setImage_1(dto.getImage_1());
        notice.setImage_2(dto.getImage_2());
        notice.setImage_3(dto.getImage_3());
        notice.setImage_4(dto.getImage_4());
        notice.setImage_5(dto.getImage_5());

        // 변경사항 저장
        noticeRepository.save(notice);
    }

    /* 공지사항 삭제 서비스 */
    public void deleteNotice(NoticeDto.Delete dto)
    {
        // 해당 공지사항이 존재하는지 확인
        Notice notice = noticeRepository.findById(dto.getNotice_id())
                .orElseThrow(() -> new AppException(ErrorCode.NOTICE_NOT_FOUND, dto.getNotice_id()));

        // 공지사항 삭제
        noticeRepository.delete(notice);
    }
}
