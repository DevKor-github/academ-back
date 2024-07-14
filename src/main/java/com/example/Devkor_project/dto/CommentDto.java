package com.example.Devkor_project.dto;

import com.example.Devkor_project.entity.Comment;
import com.example.Devkor_project.entity.CommentRating;
import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.entity.CourseRating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

public class CommentDto
{
    @AllArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class Comment
    {
        @NotNull(message = "[comment_id] cannot be null.")
        private Long comment_id;

        @NotNull(message = "[profile_id] cannot be null.")
        private Long profile_id;

        @NotNull(message = "[course_id] cannot be null.")
        private Long course_id;

        @NotBlank(message = "[review] cannot be blank.")
        private String review;
        @NotNull(message = "[likes] cannot be null.")
        private int likes;
        @NotNull(message = "[created_at] cannot be null.")
        private LocalDate created_at;
        @NotNull(message = "[updated_at] cannot be null.")
        private LocalDate updated_at;
        @NotNull(message = "[reward] cannot be null.")
        private boolean reward;

        @NotNull(message = "[rating] cannot be null.")
        private int rating;
        @NotNull(message = "[r1_amount_of_studying] cannot be null.")
        private int r1_amount_of_studying;
        @NotNull(message = "[r2_difficulty] cannot be null.")
        private int r2_difficulty;
        @NotNull(message = "[r3_delivery_power] cannot be null.")
        private int r3_delivery_power;
        @NotNull(message = "[r4_grading] cannot be null.")
        private int r4_grading;

        @NotNull(message = "[teach_t1_theory] cannot be null.")
        private boolean teach_t1_theory;
        @NotNull(message = "[teach_t2_practice] cannot be null.")
        private boolean teach_t2_practice;
        @NotNull(message = "[teach_t3_seminar] cannot be null.")
        private boolean teach_t3_seminar;
        @NotNull(message = "[teach_t4_discussion] cannot be null.")
        private boolean teach_t4_discussion;
        @NotNull(message = "[teach_t5_presentation] cannot be null.")
        private boolean teach_t5_presentation;
        @NotNull(message = "[learn_t1_theory] cannot be null.")
        private boolean learn_t1_theory;
        @NotNull(message = "[learn_t2_thesis] cannot be null.")
        private boolean learn_t2_thesis;
        @NotNull(message = "[learn_t3_exam] cannot be null.")
        private boolean learn_t3_exam;
        @NotNull(message = "[learn_t4_industry] cannot be null.")
        private boolean learn_t4_industry;
    }

    @AllArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class Detail
    {
        @NotNull(message = "[comment_id] cannot be null.")
        private Long comment_id;

        @NotNull(message = "[profile_id] cannot be null.")
        private Long profile_id;

        @NotBlank(message = "[username] cannot be blank.")
        private String username;

        @NotNull(message = "[rating] cannot be null.")
        private int rating;
        @NotNull(message = "[r1_amount_of_studying] cannot be null.")
        private int r1_amount_of_studying;
        @NotNull(message = "[r2_difficulty] cannot be null.")
        private int r2_difficulty;
        @NotNull(message = "[r3_delivery_power] cannot be null.")
        private int r3_delivery_power;
        @NotNull(message = "[r4_grading] cannot be null.")
        private int r4_grading;

        @NotBlank(message = "[review] cannot be blank.")
        private String review;

        @NotNull(message = "[teach_t1_theory] cannot be null.")
        private boolean teach_t1_theory;
        @NotNull(message = "[teach_t2_practice] cannot be null.")
        private boolean teach_t2_practice;
        @NotNull(message = "[teach_t3_seminar] cannot be null.")
        private boolean teach_t3_seminar;
        @NotNull(message = "[teach_t4_discussion] cannot be null.")
        private boolean teach_t4_discussion;
        @NotNull(message = "[teach_t5_presentation] cannot be null.")
        private boolean teach_t5_presentation;
        @NotNull(message = "[learn_t1_theory] cannot be null.")
        private boolean learn_t1_theory;
        @NotNull(message = "[learn_t2_thesis] cannot be null.")
        private boolean learn_t2_thesis;
        @NotNull(message = "[learn_t3_exam] cannot be null.")
        private boolean learn_t3_exam;
        @NotNull(message = "[learn_t4_industry] cannot be null.")
        private boolean learn_t4_industry;

        @NotNull(message = "[likes] cannot be null.")
        private int likes;
        @NotNull(message = "[created_at] cannot be null.")
        private LocalDate created_at;
        @NotNull(message = "[updated_at] cannot be null.")
        private LocalDate updated_at;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    public static class Insert
    {
        @NotNull(message = "[course_id] cannot be null.")
        private Long course_id;

        @NotNull(message = "[rating] cannot be null.")
        private int rating;
        @NotNull(message = "[r1_amount_of_studying] cannot be null.")
        private int r1_amount_of_studying;
        @NotNull(message = "[r2_difficulty] cannot be null.")
        private int r2_difficulty;
        @NotNull(message = "[r3_delivery_power] cannot be null.")
        private int r3_delivery_power;
        @NotNull(message = "[r4_grading] cannot be null.")
        private int r4_grading;

        @NotBlank(message = "[review] cannot be blank.")
        private String review;

        @NotNull(message = "[teach_t1_theory] cannot be null.")
        private boolean teach_t1_theory;
        @NotNull(message = "[teach_t2_practice] cannot be null.")
        private boolean teach_t2_practice;
        @NotNull(message = "[teach_t3_seminar] cannot be null.")
        private boolean teach_t3_seminar;
        @NotNull(message = "[teach_t4_discussion] cannot be null.")
        private boolean teach_t4_discussion;
        @NotNull(message = "[teach_t5_presentation] cannot be null.")
        private boolean teach_t5_presentation;
        @NotNull(message = "[learn_t1_theory] cannot be null.")
        private boolean learn_t1_theory;
        @NotNull(message = "[learn_t2_thesis] cannot be null.")
        private boolean learn_t2_thesis;
        @NotNull(message = "[learn_t3_exam] cannot be null.")
        private boolean learn_t3_exam;
        @NotNull(message = "[learn_t4_industry] cannot be null.")
        private boolean learn_t4_industry;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    @Builder
    public static class StartUpdate
    {
        // CourseDto.Basic
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

        // CommentDto.Update
        @NotNull(message = "[comment_id] cannot be null.")
        private Long comment_id;

        @NotNull(message = "[rating] cannot be null.")
        private int rating;
        @NotNull(message = "[r1_amount_of_studying] cannot be null.")
        private int r1_amount_of_studying;
        @NotNull(message = "[r2_difficulty] cannot be null.")
        private int r2_difficulty;
        @NotNull(message = "[r3_delivery_power] cannot be null.")
        private int r3_delivery_power;
        @NotNull(message = "[r4_grading] cannot be null.")
        private int r4_grading;

        @NotBlank(message = "[review] cannot be blank.")
        private String review;

        @NotNull(message = "[teach_t1_theory] cannot be null.")
        private boolean teach_t1_theory;
        @NotNull(message = "[teach_t2_practice] cannot be null.")
        private boolean teach_t2_practice;
        @NotNull(message = "[teach_t3_seminar] cannot be null.")
        private boolean teach_t3_seminar;
        @NotNull(message = "[teach_t4_discussion] cannot be null.")
        private boolean teach_t4_discussion;
        @NotNull(message = "[teach_t5_presentation] cannot be null.")
        private boolean teach_t5_presentation;
        @NotNull(message = "[learn_t1_theory] cannot be null.")
        private boolean learn_t1_theory;
        @NotNull(message = "[learn_t2_thesis] cannot be null.")
        private boolean learn_t2_thesis;
        @NotNull(message = "[learn_t3_exam] cannot be null.")
        private boolean learn_t3_exam;
        @NotNull(message = "[learn_t4_industry] cannot be null.")
        private boolean learn_t4_industry;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    @Builder
    public static class Update
    {
        @NotNull(message = "[comment_id] cannot be null.")
        private Long comment_id;

        @NotNull(message = "[rating] cannot be null.")
        private int rating;
        @NotNull(message = "[r1_amount_of_studying] cannot be null.")
        private int r1_amount_of_studying;
        @NotNull(message = "[r2_difficulty] cannot be null.")
        private int r2_difficulty;
        @NotNull(message = "[r3_delivery_power] cannot be null.")
        private int r3_delivery_power;
        @NotNull(message = "[r4_grading] cannot be null.")
        private int r4_grading;

        @NotBlank(message = "[review] cannot be blank.")
        private String review;

        @NotNull(message = "[teach_t1_theory] cannot be null.")
        private boolean teach_t1_theory;
        @NotNull(message = "[teach_t2_practice] cannot be null.")
        private boolean teach_t2_practice;
        @NotNull(message = "[teach_t3_seminar] cannot be null.")
        private boolean teach_t3_seminar;
        @NotNull(message = "[teach_t4_discussion] cannot be null.")
        private boolean teach_t4_discussion;
        @NotNull(message = "[teach_t5_presentation] cannot be null.")
        private boolean teach_t5_presentation;
        @NotNull(message = "[learn_t1_theory] cannot be null.")
        private boolean learn_t1_theory;
        @NotNull(message = "[learn_t2_thesis] cannot be null.")
        private boolean learn_t2_thesis;
        @NotNull(message = "[learn_t3_exam] cannot be null.")
        private boolean learn_t3_exam;
        @NotNull(message = "[learn_t4_industry] cannot be null.")
        private boolean learn_t4_industry;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Builder
    public static class Delete
    {
        @NotNull(message = "[comment_id] cannot be null.")
        private Long comment_id;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Builder
    public static class Like
    {
        @NotNull(message = "[comment_id] cannot be null.")
        private Long comment_id;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Builder
    public static class Report
    {
        @NotNull(message = "[comment_id] cannot be null.")
        private Long comment_id;

        @NotBlank(message = "[reason] cannot be blank.")
        private String reason;

        @NotBlank(message = "[detail] cannot be blank.")
        private String detail;
    }

    @AllArgsConstructor
    @Getter
    @ToString
    @Builder
    public static class MyPage
    {
        @NotNull(message = "[comment_id] cannot be null.")
        private Long comment_id;

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

        @NotBlank(message = "[review] cannot be blank.")
        private String review;
        @NotNull(message = "[likes] cannot be null.")
        private int likes;
        @NotNull(message = "[created_at] cannot be null.")
        private LocalDate created_at;
        @NotNull(message = "[updated_at] cannot be null.")
        private LocalDate updated_at;
        @NotNull(message = "[reward] cannot be null.")
        private boolean reward;

        @NotNull(message = "[rating] cannot be null.")
        private int rating;
        @NotNull(message = "[r1_amount_of_studying] cannot be null.")
        private int r1_amount_of_studying;
        @NotNull(message = "[r2_difficulty] cannot be null.")
        private int r2_difficulty;
        @NotNull(message = "[r3_delivery_power] cannot be null.")
        private int r3_delivery_power;
        @NotNull(message = "[r4_grading] cannot be null.")
        private int r4_grading;

        @NotNull(message = "[teach_t1_theory] cannot be null.")
        private boolean teach_t1_theory;
        @NotNull(message = "[teach_t2_practice] cannot be null.")
        private boolean teach_t2_practice;
        @NotNull(message = "[teach_t3_seminar] cannot be null.")
        private boolean teach_t3_seminar;
        @NotNull(message = "[teach_t4_discussion] cannot be null.")
        private boolean teach_t4_discussion;
        @NotNull(message = "[teach_t5_presentation] cannot be null.")
        private boolean teach_t5_presentation;
        @NotNull(message = "[learn_t1_theory] cannot be null.")
        private boolean learn_t1_theory;
        @NotNull(message = "[learn_t2_thesis] cannot be null.")
        private boolean learn_t2_thesis;
        @NotNull(message = "[learn_t3_exam] cannot be null.")
        private boolean learn_t3_exam;
        @NotNull(message = "[learn_t4_industry] cannot be null.")
        private boolean learn_t4_industry;
    }

    public static CommentDto.StartUpdate entityToStartUpdate(Course course,
                                                             CourseRating courseRating,
                                                             com.example.Devkor_project.entity.Comment comment,
                                                             CommentRating commentRating)
    {
        return CommentDto.StartUpdate.builder()
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
                .comment_id(comment.getComment_id())
                .rating(commentRating.getRating())
                .r1_amount_of_studying(commentRating.getR1_amount_of_studying())
                .r2_difficulty(commentRating.getR2_difficulty())
                .r3_delivery_power(commentRating.getR3_delivery_power())
                .r4_grading(commentRating.getR4_grading())
                .review(comment.getReview())
                .teach_t1_theory(commentRating.isTeach_t1_theory())
                .teach_t2_practice(commentRating.isTeach_t2_practice())
                .teach_t3_seminar(commentRating.isTeach_t3_seminar())
                .teach_t4_discussion(commentRating.isTeach_t4_discussion())
                .teach_t5_presentation(commentRating.isTeach_t5_presentation())
                .learn_t1_theory(commentRating.isLearn_t1_theory())
                .learn_t2_thesis(commentRating.isLearn_t2_thesis())
                .learn_t3_exam(commentRating.isLearn_t3_exam())
                .learn_t4_industry(commentRating.isLearn_t4_industry())
                .build();
    }

    public static CommentDto.MyPage entityToMyPage(com.example.Devkor_project.entity.Comment comment,
                                                   Course course,
                                                   CommentRating commentRating)
    {
        return MyPage.builder()
                .comment_id(comment.getComment_id())
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
                .review(comment.getReview())
                .likes(comment.getLikes())
                .created_at(comment.getCreated_at())
                .updated_at(comment.getUpdated_at())
                .reward(comment.isReward())
                .rating(commentRating.getRating())
                .r1_amount_of_studying(commentRating.getR1_amount_of_studying())
                .r2_difficulty(commentRating.getR2_difficulty())
                .r3_delivery_power(commentRating.getR3_delivery_power())
                .r4_grading(commentRating.getR4_grading())
                .teach_t1_theory(commentRating.isTeach_t1_theory())
                .teach_t2_practice(commentRating.isTeach_t2_practice())
                .teach_t3_seminar(commentRating.isTeach_t3_seminar())
                .teach_t4_discussion(commentRating.isTeach_t4_discussion())
                .teach_t5_presentation(commentRating.isTeach_t5_presentation())
                .learn_t1_theory(commentRating.isLearn_t1_theory())
                .learn_t2_thesis(commentRating.isLearn_t2_thesis())
                .learn_t3_exam(commentRating.isLearn_t3_exam())
                .learn_t4_industry(commentRating.isLearn_t4_industry())
                .build();
    }
}

