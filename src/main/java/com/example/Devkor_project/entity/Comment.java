package com.example.Devkor_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Comment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comment_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Profile profile_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Course course_id;

    @Column(nullable = false) private int rating;
    @Column(nullable = false) private int r1_amount_of_studying;
    @Column(nullable = false) private int r2_difficulty;
    @Column(nullable = false) private int r3_delivery_power;
    @Column(nullable = false) private int r4_grading;
    @Column(columnDefinition = "TEXT", nullable = false) private String review;
    @Column(nullable = false) private boolean teach_t1_theory;
    @Column(nullable = false) private boolean teach_t2_practice;
    @Column(nullable = false) private boolean teach_t3_seminar;
    @Column(nullable = false) private boolean teach_t4_discussion;
    @Column(nullable = false) private boolean teach_t5_presentation;
    @Column(nullable = false) private boolean learn_t1_theory;
    @Column(nullable = false) private boolean learn_t2_thesis;
    @Column(nullable = false) private boolean learn_t3_exam;
    @Column(nullable = false) private boolean learn_t4_industry;

    @Column(nullable = false) private int likes;
    @Column(nullable = false) private LocalDate created_at;
    @Column(nullable = false) private LocalDate updated_at;
}
