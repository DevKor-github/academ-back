package com.example.Devkor_project.dto;

import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.entity.CourseRating;
import com.example.Devkor_project.entity.TimeLocation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

public class CourseDto
{
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class NOT_UPDATED_Basic
    {
        @NotNull(message = "course_id는 null일 수 없습니다.")
        @Schema(description = "course_id")
        private Long course_id;
        @NotBlank(message = "course_code는 빈 문자열일 수 없습니다.")
        @Schema(description = "학수번호")
        private String course_code;
        @NotBlank(message = "class_number는 빈 문자열일 수 없습니다.")
        @Schema(description = "분반")
        private String class_number;
        @NotBlank(message = "graduate_school은 빈 문자열일 수 없습니다.")
        @Schema(description = "대학원")
        private String graduate_school;
        @NotBlank(message = "department는 빈 문자열일 수 없습니다.")
        @Schema(description = "학과")
        private String department;
        @NotBlank(message = "year은 빈 문자열일 수 없습니다.")
        @Schema(description = "연도")
        private String year;
        @NotBlank(message = "semester은 빈 문자열일 수 없습니다.")
        @Schema(description = "학기")
        private String semester;
        @NotBlank(message = "name은 빈 문자열일 수 없습니다.")
        @Schema(description = "강의명")
        private String name;
        @NotBlank(message = "professor은 빈 문자열일 수 없습니다.")
        @Schema(description = "교수명")
        private String professor;
        @Schema(description = "학점")
        private String credit;
        @Schema(description = "시간, 장소")
        private String time_location;
        @NotNull(message = "COUNT_comments는 null일 수 없습니다.")
        @Schema(description = "강의평 개수")
        private int COUNT_comments;
        @Schema(description = "북마크 여부")
        private Boolean isBookmark;

        @NotNull(message = "AVG_rating은 null일 수 없습니다.")
        @Schema(description = "평균 평점")
        private double AVG_rating;
        @NotNull(message = "AVG_r1_amount_of_studying은 null일 수 없습니다.")
        @Schema(description = "평균 학습량")
        private double AVG_r1_amount_of_studying;
        @NotNull(message = "AVG_r2_difficulty는 null일 수 없습니다.")
        @Schema(description = "평균 난이도")
        private double AVG_r2_difficulty;
        @NotNull(message = "AVG_r3_delivery_power은 null일 수 없습니다.")
        @Schema(description = "평균 전달력")
        private double AVG_r3_delivery_power;
        @NotNull(message = "AVG_r4_grading은 null일 수 없습니다.")
        @Schema(description = "평균 성적 관대함")
        private double AVG_r4_grading;

        @NotNull(message = "COUNT_teach_t1_theory는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-이론 강의 태그 개수")
        private int COUNT_teach_t1_theory;
        @NotNull(message = "COUNT_teach_t2_practice는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-실습 및 실험 태그 개수")
        private int COUNT_teach_t2_practice;
        @NotNull(message = "COUNT_teach_t3_seminar는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-세미나 태그 개수")
        private int COUNT_teach_t3_seminar;
        @NotNull(message = "COUNT_teach_t4_discussion는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-토론 태그 개수")
        private int COUNT_teach_t4_discussion;
        @NotNull(message = "COUNT_teach_t5_presentation는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-발표 태그 개수")
        private int COUNT_teach_t5_presentation;
        @NotNull(message = "COUNT_learn_t1_theory는 null일 수 없습니다.")
        @Schema(description = "학습 내용-이론 지식 습득 태그 개수")
        private int COUNT_learn_t1_theory;
        @NotNull(message = "COUNT_learn_t2_thesis는 null일 수 없습니다.")
        @Schema(description = "학습 내용-논문 작성 도움 태그 개수")
        private int COUNT_learn_t2_thesis;
        @NotNull(message = "COUNT_learn_t3_exam는 null일 수 없습니다.")
        @Schema(description = "학습 내용-졸업 시험 대비 태그 개수")
        private int COUNT_learn_t3_exam;
        @NotNull(message = "COUNT_learn_t4_industry는 null일 수 없습니다.")
        @Schema(description = "학습 내용-현업 적용 태그 개수")
        private int COUNT_learn_t4_industry;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class UPDATED_Basic
    {
        @NotNull(message = "course_id는 null일 수 없습니다.")
        @Schema(description = "course_id")
        private Long course_id;
        @NotBlank(message = "course_code는 빈 문자열일 수 없습니다.")
        @Schema(description = "학수번호")
        private String course_code;
        @NotBlank(message = "class_number는 빈 문자열일 수 없습니다.")
        @Schema(description = "분반")
        private String class_number;
        @NotBlank(message = "graduate_school은 빈 문자열일 수 없습니다.")
        @Schema(description = "대학원")
        private String graduate_school;
        @NotBlank(message = "department는 빈 문자열일 수 없습니다.")
        @Schema(description = "학과")
        private String department;
        @NotBlank(message = "year은 빈 문자열일 수 없습니다.")
        @Schema(description = "연도")
        private String year;
        @NotBlank(message = "semester은 빈 문자열일 수 없습니다.")
        @Schema(description = "학기")
        private String semester;
        @NotBlank(message = "name은 빈 문자열일 수 없습니다.")
        @Schema(description = "강의명")
        private String name;
        @NotBlank(message = "professor은 빈 문자열일 수 없습니다.")
        @Schema(description = "교수명")
        private String professor;
        @Schema(description = "학점")
        private String credit;
        @Schema(description = "시간, 장소")
        private List<CourseDto.TimeLocation> time_locations;
        @NotNull(message = "COUNT_comments는 null일 수 없습니다.")
        @Schema(description = "강의평 개수")
        private int COUNT_comments;
        @Schema(description = "북마크 여부")
        private Boolean isBookmark;

        @NotNull(message = "AVG_rating은 null일 수 없습니다.")
        @Schema(description = "평균 평점")
        private double AVG_rating;
        @NotNull(message = "AVG_r1_amount_of_studying은 null일 수 없습니다.")
        @Schema(description = "평균 학습량")
        private double AVG_r1_amount_of_studying;
        @NotNull(message = "AVG_r2_difficulty는 null일 수 없습니다.")
        @Schema(description = "평균 난이도")
        private double AVG_r2_difficulty;
        @NotNull(message = "AVG_r3_delivery_power은 null일 수 없습니다.")
        @Schema(description = "평균 전달력")
        private double AVG_r3_delivery_power;
        @NotNull(message = "AVG_r4_grading은 null일 수 없습니다.")
        @Schema(description = "평균 성적 관대함")
        private double AVG_r4_grading;

        @NotNull(message = "COUNT_teach_t1_theory는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-이론 강의 태그 개수")
        private int COUNT_teach_t1_theory;
        @NotNull(message = "COUNT_teach_t2_practice는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-실습 및 실험 태그 개수")
        private int COUNT_teach_t2_practice;
        @NotNull(message = "COUNT_teach_t3_seminar는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-세미나 태그 개수")
        private int COUNT_teach_t3_seminar;
        @NotNull(message = "COUNT_teach_t4_discussion는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-토론 태그 개수")
        private int COUNT_teach_t4_discussion;
        @NotNull(message = "COUNT_teach_t5_presentation는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-발표 태그 개수")
        private int COUNT_teach_t5_presentation;
        @NotNull(message = "COUNT_learn_t1_theory는 null일 수 없습니다.")
        @Schema(description = "학습 내용-이론 지식 습득 태그 개수")
        private int COUNT_learn_t1_theory;
        @NotNull(message = "COUNT_learn_t2_thesis는 null일 수 없습니다.")
        @Schema(description = "학습 내용-논문 작성 도움 태그 개수")
        private int COUNT_learn_t2_thesis;
        @NotNull(message = "COUNT_learn_t3_exam는 null일 수 없습니다.")
        @Schema(description = "학습 내용-졸업 시험 대비 태그 개수")
        private int COUNT_learn_t3_exam;
        @NotNull(message = "COUNT_learn_t4_industry는 null일 수 없습니다.")
        @Schema(description = "학습 내용-현업 적용 태그 개수")
        private int COUNT_learn_t4_industry;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class NOT_UPDATED_ExpiredBasic
    {
        @NotNull(message = "course_id는 null일 수 없습니다.")
        @Schema(description = "course_id")
        private Long course_id;
        @NotBlank(message = "course_code는 빈 문자열일 수 없습니다.")
        @Schema(description = "학수번호")
        private String course_code;
        @NotBlank(message = "class_number는 빈 문자열일 수 없습니다.")
        @Schema(description = "분반")
        private String class_number;
        @NotBlank(message = "graduate_school은 빈 문자열일 수 없습니다.")
        @Schema(description = "대학원")
        private String graduate_school;
        @NotBlank(message = "department는 빈 문자열일 수 없습니다.")
        @Schema(description = "학과")
        private String department;
        @NotBlank(message = "year은 빈 문자열일 수 없습니다.")
        @Schema(description = "연도")
        private String year;
        @NotBlank(message = "semester은 빈 문자열일 수 없습니다.")
        @Schema(description = "학기")
        private String semester;
        @NotBlank(message = "name은 빈 문자열일 수 없습니다.")
        @Schema(description = "강의명")
        private String name;
        @NotBlank(message = "professor은 빈 문자열일 수 없습니다.")
        @Schema(description = "교수명")
        private String professor;
        @Schema(description = "학점")
        private String credit;
        @Schema(description = "시간, 장소")
        private String time_location;
        @NotNull(message = "COUNT_comments는 null일 수 없습니다.")
        @Schema(description = "강의평 개수")
        private int COUNT_comments;
        @Schema(description = "북마크 여부")
        private Boolean isBookmark;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class UPDATED_ExpiredBasic
    {
        @NotNull(message = "course_id는 null일 수 없습니다.")
        @Schema(description = "course_id")
        private Long course_id;
        @NotBlank(message = "course_code는 빈 문자열일 수 없습니다.")
        @Schema(description = "학수번호")
        private String course_code;
        @NotBlank(message = "class_number는 빈 문자열일 수 없습니다.")
        @Schema(description = "분반")
        private String class_number;
        @NotBlank(message = "graduate_school은 빈 문자열일 수 없습니다.")
        @Schema(description = "대학원")
        private String graduate_school;
        @NotBlank(message = "department는 빈 문자열일 수 없습니다.")
        @Schema(description = "학과")
        private String department;
        @NotBlank(message = "year은 빈 문자열일 수 없습니다.")
        @Schema(description = "연도")
        private String year;
        @NotBlank(message = "semester은 빈 문자열일 수 없습니다.")
        @Schema(description = "학기")
        private String semester;
        @NotBlank(message = "name은 빈 문자열일 수 없습니다.")
        @Schema(description = "강의명")
        private String name;
        @NotBlank(message = "professor은 빈 문자열일 수 없습니다.")
        @Schema(description = "교수명")
        private String professor;
        @Schema(description = "학점")
        private String credit;
        @Schema(description = "시간, 장소")
        private List<CourseDto.TimeLocation> time_locations;
        @NotNull(message = "COUNT_comments는 null일 수 없습니다.")
        @Schema(description = "강의평 개수")
        private int COUNT_comments;
        @Schema(description = "북마크 여부")
        private Boolean isBookmark;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class NOT_UPDATED_Detail
    {
        @NotNull(message = "course_id는 null일 수 없습니다.")
        @Schema(description = "course_id")
        private Long course_id;
        @NotBlank(message = "course_code는 빈 문자열일 수 없습니다.")
        @Schema(description = "학수번호")
        private String course_code;
        @NotBlank(message = "class_number는 빈 문자열일 수 없습니다.")
        @Schema(description = "분반")
        private String class_number;
        @NotBlank(message = "graduate_school은 빈 문자열일 수 없습니다.")
        @Schema(description = "대학원")
        private String graduate_school;
        @NotBlank(message = "department는 빈 문자열일 수 없습니다.")
        @Schema(description = "학과")
        private String department;
        @NotBlank(message = "year은 빈 문자열일 수 없습니다.")
        @Schema(description = "연도")
        private String year;
        @NotBlank(message = "semester은 빈 문자열일 수 없습니다.")
        @Schema(description = "학기")
        private String semester;
        @NotBlank(message = "name은 빈 문자열일 수 없습니다.")
        @Schema(description = "강의명")
        private String name;
        @NotBlank(message = "professor은 빈 문자열일 수 없습니다.")
        @Schema(description = "교수명")
        private String professor;
        @Schema(description = "학점")
        private String credit;
        @Schema(description = "시간, 장소")
        private String time_location;
        @NotNull(message = "COUNT_comments는 null일 수 없습니다.")
        @Schema(description = "강의평 개수")
        private int COUNT_comments;
        @Schema(description = "북마크 여부")
        private Boolean isBookmark;

        @NotNull(message = "AVG_rating은 null일 수 없습니다.")
        @Schema(description = "평균 평점")
        private double AVG_rating;
        @NotNull(message = "AVG_r1_amount_of_studying은 null일 수 없습니다.")
        @Schema(description = "평균 학습량")
        private double AVG_r1_amount_of_studying;
        @NotNull(message = "AVG_r2_difficulty는 null일 수 없습니다.")
        @Schema(description = "평균 난이도")
        private double AVG_r2_difficulty;
        @NotNull(message = "AVG_r3_delivery_power은 null일 수 없습니다.")
        @Schema(description = "평균 전달력")
        private double AVG_r3_delivery_power;
        @NotNull(message = "AVG_r4_grading은 null일 수 없습니다.")
        @Schema(description = "평균 성적 관대함")
        private double AVG_r4_grading;

        @NotNull(message = "COUNT_teach_t1_theory는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-이론 강의 태그 개수")
        private int COUNT_teach_t1_theory;
        @NotNull(message = "COUNT_teach_t2_practice는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-실습 및 실험 태그 개수")
        private int COUNT_teach_t2_practice;
        @NotNull(message = "COUNT_teach_t3_seminar는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-세미나 태그 개수")
        private int COUNT_teach_t3_seminar;
        @NotNull(message = "COUNT_teach_t4_discussion는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-토론 태그 개수")
        private int COUNT_teach_t4_discussion;
        @NotNull(message = "COUNT_teach_t5_presentation는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-발표 태그 개수")
        private int COUNT_teach_t5_presentation;
        @NotNull(message = "COUNT_learn_t1_theory는 null일 수 없습니다.")
        @Schema(description = "학습 내용-이론 지식 습득 태그 개수")
        private int COUNT_learn_t1_theory;
        @NotNull(message = "COUNT_learn_t2_thesis는 null일 수 없습니다.")
        @Schema(description = "학습 내용-논문 작성 도움 태그 개수")
        private int COUNT_learn_t2_thesis;
        @NotNull(message = "COUNT_learn_t3_exam는 null일 수 없습니다.")
        @Schema(description = "학습 내용-졸업 시험 대비 태그 개수")
        private int COUNT_learn_t3_exam;
        @NotNull(message = "COUNT_learn_t4_industry는 null일 수 없습니다.")
        @Schema(description = "학습 내용-현업 적용 태그 개수")
        private int COUNT_learn_t4_industry;

        @Schema(description = "강의평 리스트")
        private List<CommentDto.Detail> comments;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class UPDATED_Detail
    {
        @NotNull(message = "course_id는 null일 수 없습니다.")
        @Schema(description = "course_id")
        private Long course_id;
        @NotBlank(message = "course_code는 빈 문자열일 수 없습니다.")
        @Schema(description = "학수번호")
        private String course_code;
        @NotBlank(message = "class_number는 빈 문자열일 수 없습니다.")
        @Schema(description = "분반")
        private String class_number;
        @NotBlank(message = "graduate_school은 빈 문자열일 수 없습니다.")
        @Schema(description = "대학원")
        private String graduate_school;
        @NotBlank(message = "department는 빈 문자열일 수 없습니다.")
        @Schema(description = "학과")
        private String department;
        @NotBlank(message = "year은 빈 문자열일 수 없습니다.")
        @Schema(description = "연도")
        private String year;
        @NotBlank(message = "semester은 빈 문자열일 수 없습니다.")
        @Schema(description = "학기")
        private String semester;
        @NotBlank(message = "name은 빈 문자열일 수 없습니다.")
        @Schema(description = "강의명")
        private String name;
        @NotBlank(message = "professor은 빈 문자열일 수 없습니다.")
        @Schema(description = "교수명")
        private String professor;
        @Schema(description = "학점")
        private String credit;
        @Schema(description = "시간, 장소")
        private List<CourseDto.TimeLocation> time_locations;
        @NotNull(message = "COUNT_comments는 null일 수 없습니다.")
        @Schema(description = "강의평 개수")
        private int COUNT_comments;
        @Schema(description = "북마크 여부")
        private Boolean isBookmark;

        @NotNull(message = "AVG_rating은 null일 수 없습니다.")
        @Schema(description = "평균 평점")
        private double AVG_rating;
        @NotNull(message = "AVG_r1_amount_of_studying은 null일 수 없습니다.")
        @Schema(description = "평균 학습량")
        private double AVG_r1_amount_of_studying;
        @NotNull(message = "AVG_r2_difficulty는 null일 수 없습니다.")
        @Schema(description = "평균 난이도")
        private double AVG_r2_difficulty;
        @NotNull(message = "AVG_r3_delivery_power은 null일 수 없습니다.")
        @Schema(description = "평균 전달력")
        private double AVG_r3_delivery_power;
        @NotNull(message = "AVG_r4_grading은 null일 수 없습니다.")
        @Schema(description = "평균 성적 관대함")
        private double AVG_r4_grading;

        @NotNull(message = "COUNT_teach_t1_theory는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-이론 강의 태그 개수")
        private int COUNT_teach_t1_theory;
        @NotNull(message = "COUNT_teach_t2_practice는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-실습 및 실험 태그 개수")
        private int COUNT_teach_t2_practice;
        @NotNull(message = "COUNT_teach_t3_seminar는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-세미나 태그 개수")
        private int COUNT_teach_t3_seminar;
        @NotNull(message = "COUNT_teach_t4_discussion는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-토론 태그 개수")
        private int COUNT_teach_t4_discussion;
        @NotNull(message = "COUNT_teach_t5_presentation는 null일 수 없습니다.")
        @Schema(description = "수업 진행방식-발표 태그 개수")
        private int COUNT_teach_t5_presentation;
        @NotNull(message = "COUNT_learn_t1_theory는 null일 수 없습니다.")
        @Schema(description = "학습 내용-이론 지식 습득 태그 개수")
        private int COUNT_learn_t1_theory;
        @NotNull(message = "COUNT_learn_t2_thesis는 null일 수 없습니다.")
        @Schema(description = "학습 내용-논문 작성 도움 태그 개수")
        private int COUNT_learn_t2_thesis;
        @NotNull(message = "COUNT_learn_t3_exam는 null일 수 없습니다.")
        @Schema(description = "학습 내용-졸업 시험 대비 태그 개수")
        private int COUNT_learn_t3_exam;
        @NotNull(message = "COUNT_learn_t4_industry는 null일 수 없습니다.")
        @Schema(description = "학습 내용-현업 적용 태그 개수")
        private int COUNT_learn_t4_industry;

        @Schema(description = "강의평 리스트")
        private List<CommentDto.Detail> comments;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class NOT_UPDATED_ExpiredDetail
    {
        @NotNull(message = "course_id는 null일 수 없습니다.")
        @Schema(description = "course_id")
        private Long course_id;
        @NotBlank(message = "course_code는 빈 문자열일 수 없습니다.")
        @Schema(description = "학수번호")
        private String course_code;
        @NotBlank(message = "class_number는 빈 문자열일 수 없습니다.")
        @Schema(description = "분반")
        private String class_number;
        @NotBlank(message = "graduate_school은 빈 문자열일 수 없습니다.")
        @Schema(description = "대학원")
        private String graduate_school;
        @NotBlank(message = "department는 빈 문자열일 수 없습니다.")
        @Schema(description = "학과")
        private String department;
        @NotBlank(message = "year은 빈 문자열일 수 없습니다.")
        @Schema(description = "연도")
        private String year;
        @NotBlank(message = "semester은 빈 문자열일 수 없습니다.")
        @Schema(description = "학기")
        private String semester;
        @NotBlank(message = "name은 빈 문자열일 수 없습니다.")
        @Schema(description = "강의명")
        private String name;
        @NotBlank(message = "professor은 빈 문자열일 수 없습니다.")
        @Schema(description = "교수명")
        private String professor;
        @Schema(description = "학점")
        private String credit;
        @Schema(description = "시간, 장소")
        private String time_location;
        @NotNull(message = "COUNT_comments는 null일 수 없습니다.")
        @Schema(description = "강의평 개수")
        private int COUNT_comments;
        @Schema(description = "북마크 여부")
        private Boolean isBookmark;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class UPDATED_ExpiredDetail
    {
        @NotNull(message = "course_id는 null일 수 없습니다.")
        @Schema(description = "course_id")
        private Long course_id;
        @NotBlank(message = "course_code는 빈 문자열일 수 없습니다.")
        @Schema(description = "학수번호")
        private String course_code;
        @NotBlank(message = "class_number는 빈 문자열일 수 없습니다.")
        @Schema(description = "분반")
        private String class_number;
        @NotBlank(message = "graduate_school은 빈 문자열일 수 없습니다.")
        @Schema(description = "대학원")
        private String graduate_school;
        @NotBlank(message = "department는 빈 문자열일 수 없습니다.")
        @Schema(description = "학과")
        private String department;
        @NotBlank(message = "year은 빈 문자열일 수 없습니다.")
        @Schema(description = "연도")
        private String year;
        @NotBlank(message = "semester은 빈 문자열일 수 없습니다.")
        @Schema(description = "학기")
        private String semester;
        @NotBlank(message = "name은 빈 문자열일 수 없습니다.")
        @Schema(description = "강의명")
        private String name;
        @NotBlank(message = "professor은 빈 문자열일 수 없습니다.")
        @Schema(description = "교수명")
        private String professor;
        @Schema(description = "학점")
        private String credit;
        @Schema(description = "시간, 장소")
        private List<CourseDto.TimeLocation> time_locations;
        @NotNull(message = "COUNT_comments는 null일 수 없습니다.")
        @Schema(description = "강의평 개수")
        private int COUNT_comments;
        @Schema(description = "북마크 여부")
        private Boolean isBookmark;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class CheckSynchronization
    {
        @NotBlank(message = "year은 빈 문자열일 수 없습니다.")
        @Schema(description = "연도")
        private String year;
        @NotBlank(message = "semester은 빈 문자열일 수 없습니다.")
        @Schema(description = "학기")
        private String semester;
        @NotNull(message = "insert_count는 null일 수 없습니다.")
        @Schema(description = "insert_count")
        private int insert_count;
        @NotNull(message = "update_count null일 수 없습니다.")
        @Schema(description = "update_count")
        private int update_count;
        @NotNull(message = "delete_count null일 수 없습니다.")
        @Schema(description = "delete_count")
        private int delete_count;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class TimeLocation
    {
        @Schema(description = "요일")
        private String day;
        @Schema(description = "시작 교시")
        private Integer startPeriod;
        @Schema(description = "끝 교시")
        private Integer endPeriod;
        @Schema(description = "강의실")
        private String location;
    }

    public static CourseDto.NOT_UPDATED_Basic NOT_UPDATED_entityToBasic(
            Course course,
            CourseRating courseRating,
            Boolean isBookmark)
    {
        return NOT_UPDATED_Basic.builder()
                .course_id(course.getCourse_id())
                .course_code(course.getCourse_code())
                .class_number(course.getClass_number())
                .graduate_school(course.getGraduate_school())
                .department(course.getDepartment())
                .year(course.getYear())
                .semester(course.getSemester())
                .name(course.getName())
                .professor(course.getProfessor())
                .credit(course.getCredit())
                .time_location(course.getTime_location())
                .COUNT_comments(course.getCOUNT_comments())
                .isBookmark(isBookmark)
                .AVG_rating(courseRating.getAVG_rating())
                .AVG_r1_amount_of_studying(courseRating.getAVG_r1_amount_of_studying())
                .AVG_r2_difficulty(courseRating.getAVG_r2_difficulty())
                .AVG_r3_delivery_power(courseRating.getAVG_r3_delivery_power())
                .AVG_r4_grading(courseRating.getAVG_r4_grading())
                .COUNT_teach_t1_theory(courseRating.getCOUNT_teach_t1_theory())
                .COUNT_teach_t2_practice(courseRating.getCOUNT_teach_t2_practice())
                .COUNT_teach_t3_seminar(courseRating.getCOUNT_teach_t3_seminar())
                .COUNT_teach_t4_discussion(courseRating.getCOUNT_teach_t4_discussion())
                .COUNT_teach_t5_presentation(courseRating.getCOUNT_teach_t5_presentation())
                .COUNT_learn_t1_theory(courseRating.getCOUNT_learn_t1_theory())
                .COUNT_learn_t2_thesis(courseRating.getCOUNT_learn_t2_thesis())
                .COUNT_learn_t3_exam(courseRating.getCOUNT_learn_t3_exam())
                .COUNT_learn_t4_industry(courseRating.getCOUNT_learn_t4_industry())
                .build();
    }

    public static CourseDto.UPDATED_Basic UPDATED_entityToBasic(
            Course course,
            CourseRating courseRating,
            List<com.example.Devkor_project.entity.TimeLocation> timeLocations,
            Boolean isBookmark
    )
    {
        return UPDATED_Basic.builder()
                .course_id(course.getCourse_id())
                .course_code(course.getCourse_code())
                .class_number(course.getClass_number())
                .graduate_school(course.getGraduate_school())
                .department(course.getDepartment())
                .year(course.getYear())
                .semester(course.getSemester())
                .name(course.getName())
                .professor(course.getProfessor())
                .credit(course.getCredit())
                .time_locations(
                        timeLocations.stream().map(
                                        timeLocation -> {
                                            return CourseDto.TimeLocation.builder()
                                                    .day(timeLocation.getDay())
                                                    .startPeriod(timeLocation.getStartPeriod())
                                                    .endPeriod(timeLocation.getEndPeriod())
                                                    .location(timeLocation.getLocation())
                                                    .build();
                                        })
                                .toList()
                )
                .COUNT_comments(course.getCOUNT_comments())
                .isBookmark(isBookmark)
                .AVG_rating(courseRating.getAVG_rating())
                .AVG_r1_amount_of_studying(courseRating.getAVG_r1_amount_of_studying())
                .AVG_r2_difficulty(courseRating.getAVG_r2_difficulty())
                .AVG_r3_delivery_power(courseRating.getAVG_r3_delivery_power())
                .AVG_r4_grading(courseRating.getAVG_r4_grading())
                .COUNT_teach_t1_theory(courseRating.getCOUNT_teach_t1_theory())
                .COUNT_teach_t2_practice(courseRating.getCOUNT_teach_t2_practice())
                .COUNT_teach_t3_seminar(courseRating.getCOUNT_teach_t3_seminar())
                .COUNT_teach_t4_discussion(courseRating.getCOUNT_teach_t4_discussion())
                .COUNT_teach_t5_presentation(courseRating.getCOUNT_teach_t5_presentation())
                .COUNT_learn_t1_theory(courseRating.getCOUNT_learn_t1_theory())
                .COUNT_learn_t2_thesis(courseRating.getCOUNT_learn_t2_thesis())
                .COUNT_learn_t3_exam(courseRating.getCOUNT_learn_t3_exam())
                .COUNT_learn_t4_industry(courseRating.getCOUNT_learn_t4_industry())
                .build();
    }

    public static CourseDto.NOT_UPDATED_ExpiredBasic NOT_UPDATED_entityToExpiredBasic(
            Course course,
            Boolean isBookmark)
    {
        return NOT_UPDATED_ExpiredBasic.builder()
                .course_id(course.getCourse_id())
                .course_code(course.getCourse_code())
                .class_number(course.getClass_number())
                .graduate_school(course.getGraduate_school())
                .department(course.getDepartment())
                .year(course.getYear())
                .semester(course.getSemester())
                .name(course.getName())
                .professor(course.getProfessor())
                .credit(course.getCredit())
                .time_location(course.getTime_location())
                .COUNT_comments(course.getCOUNT_comments())
                .isBookmark(isBookmark)
                .build();
    }

    public static CourseDto.UPDATED_ExpiredBasic UPDATED_entityToExpiredBasic(
            Course course,
            List<com.example.Devkor_project.entity.TimeLocation> timeLocations,
            Boolean isBookmark
    )
    {
        return UPDATED_ExpiredBasic.builder()
                .course_id(course.getCourse_id())
                .course_code(course.getCourse_code())
                .class_number(course.getClass_number())
                .graduate_school(course.getGraduate_school())
                .department(course.getDepartment())
                .year(course.getYear())
                .semester(course.getSemester())
                .name(course.getName())
                .professor(course.getProfessor())
                .credit(course.getCredit())
                .time_locations(
                        timeLocations.stream().map(
                        timeLocation -> {
                            return CourseDto.TimeLocation.builder()
                                    .day(timeLocation.getDay())
                                    .startPeriod(timeLocation.getStartPeriod())
                                    .endPeriod(timeLocation.getEndPeriod())
                                    .location(timeLocation.getLocation())
                                    .build();
                        })
                        .toList()
                )
                .COUNT_comments(course.getCOUNT_comments())
                .isBookmark(isBookmark)
                .build();
    }

    public static CourseDto.NOT_UPDATED_Detail NOT_UPDATED_entityToDetail(
            Course course,
            CourseRating courseRating,
            List<CommentDto.Detail> commentDtos,
            Boolean isBookmark)
    {
        return NOT_UPDATED_Detail.builder()
                .course_id(course.getCourse_id())
                .course_code(course.getCourse_code())
                .class_number(course.getClass_number())
                .graduate_school(course.getGraduate_school())
                .department(course.getDepartment())
                .year(course.getYear())
                .semester(course.getSemester())
                .name(course.getName())
                .professor(course.getProfessor())
                .credit(course.getCredit())
                .time_location(course.getTime_location())
                .COUNT_comments(course.getCOUNT_comments())
                .isBookmark(isBookmark)
                .AVG_rating(courseRating.getAVG_rating())
                .AVG_r1_amount_of_studying(courseRating.getAVG_r1_amount_of_studying())
                .AVG_r2_difficulty(courseRating.getAVG_r2_difficulty())
                .AVG_r3_delivery_power(courseRating.getAVG_r3_delivery_power())
                .AVG_r4_grading(courseRating.getAVG_r4_grading())
                .COUNT_teach_t1_theory(courseRating.getCOUNT_teach_t1_theory())
                .COUNT_teach_t2_practice(courseRating.getCOUNT_teach_t2_practice())
                .COUNT_teach_t3_seminar(courseRating.getCOUNT_teach_t3_seminar())
                .COUNT_teach_t4_discussion(courseRating.getCOUNT_teach_t4_discussion())
                .COUNT_teach_t5_presentation(courseRating.getCOUNT_teach_t5_presentation())
                .COUNT_learn_t1_theory(courseRating.getCOUNT_learn_t1_theory())
                .COUNT_learn_t2_thesis(courseRating.getCOUNT_learn_t2_thesis())
                .COUNT_learn_t3_exam(courseRating.getCOUNT_learn_t3_exam())
                .COUNT_learn_t4_industry(courseRating.getCOUNT_learn_t4_industry())
                .comments(commentDtos)
                .build();
    }

    public static CourseDto.UPDATED_Detail UPDATED_entityToDetail(
            Course course,
            CourseRating courseRating,
            List<CommentDto.Detail> commentDtos,
            List<com.example.Devkor_project.entity.TimeLocation> timeLocations,
            Boolean isBookmark)
    {
        return UPDATED_Detail.builder()
                .course_id(course.getCourse_id())
                .course_code(course.getCourse_code())
                .class_number(course.getClass_number())
                .graduate_school(course.getGraduate_school())
                .department(course.getDepartment())
                .year(course.getYear())
                .semester(course.getSemester())
                .name(course.getName())
                .professor(course.getProfessor())
                .credit(course.getCredit())
                .time_locations(
                        timeLocations.stream().map(
                                        timeLocation -> {
                                            return CourseDto.TimeLocation.builder()
                                                    .day(timeLocation.getDay())
                                                    .startPeriod(timeLocation.getStartPeriod())
                                                    .endPeriod(timeLocation.getEndPeriod())
                                                    .location(timeLocation.getLocation())
                                                    .build();
                                        })
                                .toList()
                )
                .COUNT_comments(course.getCOUNT_comments())
                .isBookmark(isBookmark)
                .AVG_rating(courseRating.getAVG_rating())
                .AVG_r1_amount_of_studying(courseRating.getAVG_r1_amount_of_studying())
                .AVG_r2_difficulty(courseRating.getAVG_r2_difficulty())
                .AVG_r3_delivery_power(courseRating.getAVG_r3_delivery_power())
                .AVG_r4_grading(courseRating.getAVG_r4_grading())
                .COUNT_teach_t1_theory(courseRating.getCOUNT_teach_t1_theory())
                .COUNT_teach_t2_practice(courseRating.getCOUNT_teach_t2_practice())
                .COUNT_teach_t3_seminar(courseRating.getCOUNT_teach_t3_seminar())
                .COUNT_teach_t4_discussion(courseRating.getCOUNT_teach_t4_discussion())
                .COUNT_teach_t5_presentation(courseRating.getCOUNT_teach_t5_presentation())
                .COUNT_learn_t1_theory(courseRating.getCOUNT_learn_t1_theory())
                .COUNT_learn_t2_thesis(courseRating.getCOUNT_learn_t2_thesis())
                .COUNT_learn_t3_exam(courseRating.getCOUNT_learn_t3_exam())
                .COUNT_learn_t4_industry(courseRating.getCOUNT_learn_t4_industry())
                .comments(commentDtos)
                .build();
    }

    public static CourseDto.NOT_UPDATED_ExpiredDetail NOT_UPDATED_entityToExpiredDetail(
            Course course,
            Boolean isBookmark)
    {
        return NOT_UPDATED_ExpiredDetail.builder()
                .course_id(course.getCourse_id())
                .course_code(course.getCourse_code())
                .class_number(course.getClass_number())
                .graduate_school(course.getGraduate_school())
                .department(course.getDepartment())
                .year(course.getYear())
                .semester(course.getSemester())
                .name(course.getName())
                .professor(course.getProfessor())
                .credit(course.getCredit())
                .time_location(course.getTime_location())
                .COUNT_comments(course.getCOUNT_comments())
                .isBookmark(isBookmark)
                .build();
    }

    public static CourseDto.UPDATED_ExpiredDetail UPDATED_entityToExpiredDetail(
            Course course,
            List<com.example.Devkor_project.entity.TimeLocation> timeLocations,
            Boolean isBookmark)
    {
        return UPDATED_ExpiredDetail.builder()
                .course_id(course.getCourse_id())
                .course_code(course.getCourse_code())
                .class_number(course.getClass_number())
                .graduate_school(course.getGraduate_school())
                .department(course.getDepartment())
                .year(course.getYear())
                .semester(course.getSemester())
                .name(course.getName())
                .professor(course.getProfessor())
                .credit(course.getCredit())
                .time_locations(
                        timeLocations.stream().map(
                                        timeLocation -> {
                                            return CourseDto.TimeLocation.builder()
                                                    .day(timeLocation.getDay())
                                                    .startPeriod(timeLocation.getStartPeriod())
                                                    .endPeriod(timeLocation.getEndPeriod())
                                                    .location(timeLocation.getLocation())
                                                    .build();
                                        })
                                .toList()
                )
                .COUNT_comments(course.getCOUNT_comments())
                .isBookmark(isBookmark)
                .build();
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class Basic {  // ✅ 내부 static 클래스로 선언되어 있어야 함
        private Long course_id;
        private String course_code;
        private String name;
        private String professor;
    }

    public static CourseDto.Basic fromCourse(Course course) {
        return CourseDto.Basic.builder()
                .course_id(course.getCourse_id())
                .course_code(course.getCourse_code())
                .name(course.getName())
                .professor(course.getProfessor())
                .build();
    }
}
