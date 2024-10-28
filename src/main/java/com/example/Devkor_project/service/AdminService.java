package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.CommentDto;
import com.example.Devkor_project.dto.CourseDto;
import com.example.Devkor_project.dto.CrawlingDto;
import com.example.Devkor_project.dto.TrafficDto;
import com.example.Devkor_project.entity.*;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.Principal;
import java.util.*;

@Service
@Slf4j
public class AdminService
{
    @Autowired CourseRepository courseRepository;
    @Autowired CourseRatingRepository courseRatingRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired CommentReportRepository commentReportRepository;
    @Autowired TrafficRepository trafficRepository;

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

    /* 강의 정보 동기화 서비스 */
    @Transactional
    public CourseDto.CheckSynchronization checkCourseSynchronization(CrawlingDto.Synchronization dto)
    {
        WebClient webClient = WebClient.builder().build();  // HTTP 요청을 보내기 위한 WebClient 인스턴스
        ObjectMapper objectMapper = new ObjectMapper();     // 문자열을 dto로 변환하기 위한 ObjectMapper
        int count = 0;      // 동기화 되지 않은 강의 개수

        // 대학원 리스트를 요청한 후, 응답을 문자열로 저장
        String response = webClient.post()
                .uri("https://sugang.korea.ac.kr/view?attribute=combo&fake=1712483560986")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Referer", "https://sugang.korea.ac.kr/graduate/core?attribute=coreMain&flagx=X&fake=1712483556643")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData("obj", "pColCd")
                        .with("args", "KOR")
                        .with("args", dto.getYear())
                        .with("args", dto.getSemester())
                        .with("args", "1"))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            // 대학원 리스트 응답 문자열을 dto로 변환
            CrawlingDto.Response_GraduateSchool response_graduateSchool = objectMapper.readValue(response, CrawlingDto.Response_GraduateSchool.class);
            List<CrawlingDto.GraduateSchool> graduateSchools = response_graduateSchool.getData();

            for(CrawlingDto.GraduateSchool graduateSchool:graduateSchools)
            {
                // 학과 리스트를 요청한 후, 응답을 문자열로 저장
                response = webClient.post()
                        .uri("https://sugang.korea.ac.kr/view?attribute=combo&fake=1712483560986")
                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .header("Referer", "https://sugang.korea.ac.kr/graduate/core?attribute=coreMain&flagx=X&fake=1712483556643")
                        .accept(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromFormData("obj", "pDeptCd")
                                .with("args", "KOR")
                                .with("args", dto.getYear())
                                .with("args", dto.getSemester())
                                .with("args", graduateSchool.getCode())
                                .with("args", "1"))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                try {
                    // 학과 리스트 응답 문자열을 dto로 변환
                    CrawlingDto.Response_Department response_department = objectMapper.readValue(response, CrawlingDto.Response_Department.class);
                    List<CrawlingDto.Department> departments = response_department.getData();

                    for(CrawlingDto.Department department:departments)
                    {
                        // 강의 리스트를 요청한 후, 응답을 문자열로 저장
                        response = webClient.post()
                                .uri("https://sugang.korea.ac.kr/graduate/view?attribute=lectGradData&fake=1712483595039")
                                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .header("Referer", "https://sugang.korea.ac.kr/graduate/core?attribute=coreMain&flagx=X&fake=1712483556643")
                                .accept(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromFormData("pYear", dto.getYear())
                                        .with("pTerm", dto.getSemester())
                                        .with("pCampus", "1")
                                        .with("pColCd", graduateSchool.getCode())
                                        .with("pDeptCd", department.getCode()))
                                .retrieve()
                                .bodyToMono(String.class)
                                .block();

                        try {
                            // 강의 리스트 응답 문자열을 dto로 변환
                            CrawlingDto.Response_Course response_course = objectMapper.readValue(response, CrawlingDto.Response_Course.class);
                            List<CrawlingDto.Course> courses = response_course.getData();

                            for(CrawlingDto.Course course:courses)
                            {
                                // 해당 크롤링 강의 데이터와 일치하는 데이터를 데이터베이스에서 조회
                                Course coursesInDatabase = courseRepository.compareWithCrawlingData(
                                        course.getCour_nm(),
                                        course.getCour_cd(),
                                        course.getCour_cls(),
                                        course.getProf_nm(),
                                        graduateSchool.getName(),
                                        department.getName(),
                                        dto.getYear(),
                                        dto.getSemester(),
                                        course.getTime_room().isEmpty() ? null : course.getTime_room().replaceAll("<.*?>", "")
                                );

                                // 현재 데이터베이스에 해당 강의 정보가 존재하지 않는다면 강의 평점 레코드와 함께 추가
                                if(coursesInDatabase == null)
                                {
                                    // 동기화 처리 횟수 증가
                                    count++;

                                    String credit = null;           // 학점
                                    String time_location = null;    // 강의 시간, 강의실

                                    if (!course.getTime().isEmpty())
                                        credit = course.getTime().replaceAll("\\(.*?\\)", "");

                                    if (!course.getTime_room().isEmpty())
                                        time_location = course.getTime_room().replaceAll("<.*?>", "");

                                    // 강의 평점 레코드 추가
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

                                    // 강의 레코드 추가
                                    Course newCourse = Course.builder()
                                            .courseRating_id(courseRating)
                                            .name(course.getCour_nm())
                                            .course_code(course.getCour_cd())
                                            .class_number(course.getCour_cls())
                                            .professor(course.getProf_nm())
                                            .graduate_school(graduateSchool.getName())
                                            .department(department.getName())
                                            .year(course.getYear())
                                            .semester(course.getTerm())
                                            .credit(credit)
                                            .time_location(time_location)
                                            .COUNT_comments(0)
                                            .build();

                                    courseRepository.save(newCourse);
                                }
                            }
                        } catch (Exception error) {
                            throw new RuntimeException(error);
                        }
                    }
                } catch (Exception error) {
                    throw new RuntimeException(error);
                }
            }
        } catch (Exception error) {
            throw new RuntimeException(error);
        }

        return CourseDto.CheckSynchronization.builder()
                .year(dto.getYear())
                .semester(dto.getSemester())
                .count(count)
                .build();
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

    /* 월 단위 경로별 트래픽 확인 서비스 */
    public List<TrafficDto.Month> trafficMonthly(String year, Byte month)
    {
        // 해당 기간 동안의 트래픽 정보 검색
        List<Traffic> traffics = trafficRepository.findByMonth(year, month);

        // 트래픽 정보가 존재하지 않는다면 예외 발생
        if(traffics.isEmpty())
            throw new AppException(ErrorCode.TRAFFIC_NOT_FOUND, null);

        // 엔티티 리스트를 dto 리스트로 변환
        List<TrafficDto.Month> trafficDtos = traffics.stream()
                .map(traffic -> {
                    return TrafficDto.Month.builder()
                            .api_path(traffic.getApi_path())
                            .count(traffic.getCount())
                            .build();
                })
                .toList();

        return trafficDtos;
    }

    /* 연도 단위 월별 트래픽 확인 서비스 */
    public List<TrafficDto.Year> trafficYearly(String year)
    {
        // 반환할 데이터 변수
        List<TrafficDto.Year> data = new ArrayList<>();

        for(byte i = 1; i <= 12; i++)
        {
            // 총 요청 횟수를 저장하는 변수
            int total = 0;

            // 해당 월 동안의 트래픽 정보 검색
            List<Traffic> traffics = trafficRepository.findByMonth(year, i);

            // 각 트래픽 정보마다의 요청 횟수를 total 변수에 추가
            if(!traffics.isEmpty())
            {
                for (Traffic traffic : traffics) {
                    total += traffic.getCount();
                }
            }

            data.add(
                    TrafficDto.Year.builder()
                            .month(i)
                            .count(total)
                            .build()
            );
        }

        return data;
    }
}
