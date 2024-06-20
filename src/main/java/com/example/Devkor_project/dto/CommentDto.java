package com.example.Devkor_project.dto;

import jakarta.validation.constraints.Email;
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
}

