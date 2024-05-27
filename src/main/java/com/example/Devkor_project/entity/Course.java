package com.example.Devkor_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Course
{
    @Id
    private Long course_id;

    @Column(nullable = false) private String name;
    @Column(nullable = false) private String course_code;
    @Column(nullable = false) private String professor;
    @Column(nullable = false) private String graduate_school;
    @Column(nullable = false) private String department;
    @Column(nullable = false) private String year;
    @Column(nullable = false) private String semester;
    @Column private String credit;
    @Column private String time_location;

    @Column(nullable = false) private int COUNT_comments;
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
