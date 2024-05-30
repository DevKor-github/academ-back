package com.example.Devkor_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CommentRating
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentRating_id;

    @Column(nullable = false) private int rating;
    @Column(nullable = false) private int r1_amount_of_studying;
    @Column(nullable = false) private int r2_difficulty;
    @Column(nullable = false) private int r3_delivery_power;
    @Column(nullable = false) private int r4_grading;

    @Column(nullable = false) private boolean teach_t1_theory;
    @Column(nullable = false) private boolean teach_t2_practice;
    @Column(nullable = false) private boolean teach_t3_seminar;
    @Column(nullable = false) private boolean teach_t4_discussion;
    @Column(nullable = false) private boolean teach_t5_presentation;
    @Column(nullable = false) private boolean learn_t1_theory;
    @Column(nullable = false) private boolean learn_t2_thesis;
    @Column(nullable = false) private boolean learn_t3_exam;
    @Column(nullable = false) private boolean learn_t4_industry;
}
