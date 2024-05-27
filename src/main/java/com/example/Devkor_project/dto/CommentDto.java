package com.example.Devkor_project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@ToString
@Getter
@Builder
public class CommentDto
{
    @NotNull(message = "comment_id는 null일 수 없습니다.")
    private Long comment_id;

    @NotNull(message = "profile_id는 null일 수 없습니다.")
    private String profile_username;

    @NotNull(message = "rating은 null일 수 없습니다.")
    private int rating;
    @NotNull(message = "r1_amount_of_studying은 null일 수 없습니다.")
    private int r1_amount_of_studying;
    @NotNull(message = "r2_difficulty은 null일 수 없습니다.")
    private int r2_difficulty;
    @NotNull(message = "r3_delivery_power은 null일 수 없습니다.")
    private int r3_delivery_power;
    @NotNull(message = "r4_grading은 null일 수 없습니다.")
    private int r4_grading;

    @NotBlank(message = "review는 빈 문자열일 수 없습니다.")
    private String review;

    @NotNull(message = "teach_t1_theory는 null일 수 없습니다.")
    private boolean teach_t1_theory;
    @NotNull(message = "teach_t2_practice는 null일 수 없습니다.")
    private boolean teach_t2_practice;
    @NotNull(message = "teach_t3_seminar는 null일 수 없습니다.")
    private boolean teach_t3_seminar;
    @NotNull(message = "teach_t4_discussion는 null일 수 없습니다.")
    private boolean teach_t4_discussion;
    @NotNull(message = "teach_t5_presentation는 null일 수 없습니다.")
    private boolean teach_t5_presentation;
    @NotNull(message = "learn_t1_theory는 null일 수 없습니다.")
    private boolean learn_t1_theory;
    @NotNull(message = "learn_t2_thesis는 null일 수 없습니다.")
    private boolean learn_t2_thesis;
    @NotNull(message = "learn_t3_exam는 null일 수 없습니다.")
    private boolean learn_t3_exam;
    @NotNull(message = "learn_t4_industry는 null일 수 없습니다.")
    private boolean learn_t4_industry;

    @NotNull(message = "like는 null일 수 없습니다.")
    private int likes;
    @NotNull(message = "created_at은 null일 수 없습니다.")
    private LocalDate created_at;
    @NotNull(message = "updated_at은 null일 수 없습니다.")
    private LocalDate updated_at;
}

