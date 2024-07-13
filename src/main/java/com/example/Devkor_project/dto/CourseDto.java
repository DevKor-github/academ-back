package com.example.Devkor_project.dto;

import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.entity.CourseRating;
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
    public static class Basic {
        @NotNull(message = "course_id는 null일 수 없습니다.")
        private Long course_id;
        @NotBlank(message = "course_code는 빈 문자열일 수 없습니다.")
        private String course_code;
        @NotBlank(message = "graduate_school은 빈 문자열일 수 없습니다.")
        private String graduate_school;
        @NotBlank(message = "department는 빈 문자열일 수 없습니다.")
        private String department;
        @NotBlank(message = "year은 빈 문자열일 수 없습니다.")
        private String year;
        @NotBlank(message = "semester은 빈 문자열일 수 없습니다.")
        private String semester;
        @NotBlank(message = "name은 빈 문자열일 수 없습니다.")
        private String name;
        @NotBlank(message = "professor은 빈 문자열일 수 없습니다.")
        private String professor;
        private String credit;
        private String time_location;
        @NotNull(message = "COUNT_comments는 null일 수 없습니다.")
        private int COUNT_comments;

        @NotNull(message = "AVG_rating은 null일 수 없습니다.")
        private double AVG_rating;
        @NotNull(message = "AVG_r1_amount_of_studying은 null일 수 없습니다.")
        private double AVG_r1_amount_of_studying;
        @NotNull(message = "AVG_r2_difficulty는 null일 수 없습니다.")
        private double AVG_r2_difficulty;
        @NotNull(message = "AVG_r3_delivery_power은 null일 수 없습니다.")
        private double AVG_r3_delivery_power;
        @NotNull(message = "AVG_r4_grading은 null일 수 없습니다.")
        private double AVG_r4_grading;

        @NotNull(message = "COUNT_teach_t1_theory는 null일 수 없습니다.")
        private int COUNT_teach_t1_theory;
        @NotNull(message = "COUNT_teach_t2_practice는 null일 수 없습니다.")
        private int COUNT_teach_t2_practice;
        @NotNull(message = "COUNT_teach_t3_seminar는 null일 수 없습니다.")
        private int COUNT_teach_t3_seminar;
        @NotNull(message = "COUNT_teach_t4_discussion는 null일 수 없습니다.")
        private int COUNT_teach_t4_discussion;
        @NotNull(message = "COUNT_teach_t5_presentation는 null일 수 없습니다.")
        private int COUNT_teach_t5_presentation;
        @NotNull(message = "COUNT_learn_t1_theory는 null일 수 없습니다.")
        private int COUNT_learn_t1_theory;
        @NotNull(message = "COUNT_learn_t2_thesis는 null일 수 없습니다.")
        private int COUNT_learn_t2_thesis;
        @NotNull(message = "COUNT_learn_t3_exam는 null일 수 없습니다.")
        private int COUNT_learn_t3_exam;
        @NotNull(message = "COUNT_learn_t4_industry는 null일 수 없습니다.")
        private int COUNT_learn_t4_industry;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class Detail {
        @NotNull(message = "course_id는 null일 수 없습니다.")
        private Long course_id;
        @NotBlank(message = "course_code는 빈 문자열일 수 없습니다.")
        private String course_code;
        @NotBlank(message = "graduate_school은 빈 문자열일 수 없습니다.")
        private String graduate_school;
        @NotBlank(message = "department는 빈 문자열일 수 없습니다.")
        private String department;
        @NotBlank(message = "year은 빈 문자열일 수 없습니다.")
        private String year;
        @NotBlank(message = "semester은 빈 문자열일 수 없습니다.")
        private String semester;
        @NotBlank(message = "name은 빈 문자열일 수 없습니다.")
        private String name;
        @NotBlank(message = "professor은 빈 문자열일 수 없습니다.")
        private String professor;
        private String credit;
        private String time_location;

        @NotNull(message = "COUNT_comments는 null일 수 없습니다.")
        private int COUNT_comments;
        @NotNull(message = "AVG_rating은 null일 수 없습니다.")
        private double AVG_rating;
        @NotNull(message = "AVG_r1_amount_of_studying은 null일 수 없습니다.")
        private double AVG_r1_amount_of_studying;
        @NotNull(message = "AVG_r2_difficulty는 null일 수 없습니다.")
        private double AVG_r2_difficulty;
        @NotNull(message = "AVG_r3_delivery_power은 null일 수 없습니다.")
        private double AVG_r3_delivery_power;
        @NotNull(message = "AVG_r4_grading은 null일 수 없습니다.")
        private double AVG_r4_grading;

        @NotNull(message = "COUNT_teach_t1_theory는 null일 수 없습니다.")
        private int COUNT_teach_t1_theory;
        @NotNull(message = "COUNT_teach_t2_practice는 null일 수 없습니다.")
        private int COUNT_teach_t2_practice;
        @NotNull(message = "COUNT_teach_t3_seminar는 null일 수 없습니다.")
        private int COUNT_teach_t3_seminar;
        @NotNull(message = "COUNT_teach_t4_discussion는 null일 수 없습니다.")
        private int COUNT_teach_t4_discussion;
        @NotNull(message = "COUNT_teach_t5_presentation는 null일 수 없습니다.")
        private int COUNT_teach_t5_presentation;
        @NotNull(message = "COUNT_learn_t1_theory는 null일 수 없습니다.")
        private int COUNT_learn_t1_theory;
        @NotNull(message = "COUNT_learn_t2_thesis는 null일 수 없습니다.")
        private int COUNT_learn_t2_thesis;
        @NotNull(message = "COUNT_learn_t3_exam는 null일 수 없습니다.")
        private int COUNT_learn_t3_exam;
        @NotNull(message = "COUNT_learn_t4_industry는 null일 수 없습니다.")
        private int COUNT_learn_t4_industry;

        private List<CommentDto.Detail> comments;
    }

    public static CourseDto.Basic entityToBasic(Course course, CourseRating courseRating) {
        return CourseDto.Basic.builder()
                .course_id(course.getCourse_id())
                .course_code(course.getCourse_code())
                .graduate_school(course.getGraduate_school())
                .department(course.getDepartment())
                .year(course.getYear())
                .semester(course.getSemester())
                .name(course.getName())
                .professor(course.getProfessor())
                .credit(course.getCredit())
                .time_location(course.getTime_location())
                .COUNT_comments(course.getCOUNT_comments())
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

    public static CourseDto.Detail entityToDetail(Course course,
                                                  CourseRating courseRating,
                                                  List<CommentDto.Detail> commentDtos)
    {
        return CourseDto.Detail.builder()
                .course_id(course.getCourse_id())
                .course_code(course.getCourse_code())
                .graduate_school(course.getGraduate_school())
                .department(course.getDepartment())
                .year(course.getYear())
                .semester(course.getSemester())
                .name(course.getName())
                .professor(course.getProfessor())
                .credit(course.getCredit())
                .time_location(course.getTime_location())
                .COUNT_comments(course.getCOUNT_comments())
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
}
