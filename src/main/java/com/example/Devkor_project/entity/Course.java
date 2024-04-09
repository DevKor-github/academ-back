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
    private Long id;

    @Column(nullable = false) private String graduateSchool;
    @Column(nullable = false) private String department;
    @Column(nullable = false) private String year;
    @Column(nullable = false) private String semester;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String professor;
    @Column(nullable = false) private String time;
    @Column private String location;
    @Column(nullable = false) private double rating;
}
