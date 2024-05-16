package com.example.Devkor_project.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false) private double rating;
    @Column(nullable = false) private double amount_of_studying;
    @Column(nullable = false) private double difficulty;
    @Column(nullable = false) private double delivery_power;
    @Column(nullable = false) private double grading;
}
