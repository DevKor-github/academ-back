package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.CommentDto;
import com.example.Devkor_project.entity.*;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Service
public class AdminService
{
    @Autowired CourseRepository courseRepository;
    @Autowired CourseRatingRepository courseRatingRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired CommentReportRepository commentReportRepository;

    @Autowired CourseService courseService;

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

    /* 강의평 신고 내역 조회 서비스 */
    public List<CommentDto.ReportList> reportCommentList(int page)
    {
        // Pageable 객체 생성 (size = 10개)
        Pageable pageable = PageRequest.of(page, 10);
        Page<CommentReport> reports = commentReportRepository.reportsByPage(pageable);

        // 결과가 없으면 예외 발생
        if(reports.isEmpty())
            throw new AppException(ErrorCode.NO_RESULT, page + 1);

        List<CommentDto.ReportList> reportListDtos = reports.stream()
                .map(report -> {
                    return CommentDto.ReportList.builder()
                            .comment_id(report.getComment_id().getComment_id())
                            .reporter_profile_id(report.getProfile_id().getProfile_id())
                            .reporter_email(report.getProfile_id().getEmail())
                            .reporter_username(report.getProfile_id().getUsername())
                            .writer_profile_id(report.getComment_id().getProfile_id().getProfile_id())
                            .writer_email(report.getComment_id().getProfile_id().getEmail())
                            .writer_username(report.getComment_id().getProfile_id().getUsername())
                            .review(report.getComment_id().getReview())
                            .reason(report.getReason())
                            .detail(report.getDetail())
                            .created_at(report.getCreated_at())
                            .build();
                })
                .toList();

        return reportListDtos;
    }

    /* 강의평 신고 내역 개수 서비스 */
    public Long countReport()
    {
        return commentReportRepository.countAllReports();
    }

    /* 강의평 삭제 서비스 */
    public Long deleteComment(CommentDto.Delete dto)
    {
        // 강의평 데이터
        Comment comment = commentRepository.findById(dto.getComment_id())
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND, dto.getComment_id()));

        // 강의평 작성자 이메일
        String writerEmail = comment.getProfile_id().getEmail();

        // 강의평 작성자 이메일을 반환하는 새로운 Principal 객체
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return writerEmail;
            }
        };

        return courseService.deleteComment(principal, dto);
    }
}
