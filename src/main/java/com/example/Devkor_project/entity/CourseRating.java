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
public class CourseRating
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseRating_id;

    @Column(nullable = false) private double AVG_rating;
    @Column(nullable = false) private double AVG_r1_amount_of_studying;
    @Column(nullable = false) private double AVG_r2_difficulty;
    @Column(nullable = false) private double AVG_r3_delivery_power;
    @Column(nullable = false) private double AVG_r4_grading;

    @Column(nullable = false) private int COUNT_teach_t1_theory;
    @Column(nullable = false) private int COUNT_teach_t2_practice;
    @Column(nullable = false) private int COUNT_teach_t3_seminar;
    @Column(nullable = false) private int COUNT_teach_t4_discussion;
    @Column(nullable = false) private int COUNT_teach_t5_presentation;
    @Column(nullable = false) private int COUNT_learn_t1_theory;
    @Column(nullable = false) private int COUNT_learn_t2_thesis;
    @Column(nullable = false) private int COUNT_learn_t3_exam;
    @Column(nullable = false) private int COUNT_learn_t4_industry;
}
