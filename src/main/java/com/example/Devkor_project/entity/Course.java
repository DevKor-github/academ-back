package com.example.Devkor_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class Course
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long course_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseRating_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CourseRating courseRating_id;

    @Column(nullable = false) private String name;
    @Column(nullable = false) private String course_code;
    @Column(nullable = false) private String class_number;
    @Column(nullable = false) private String professor;
    @Column(nullable = false) private String graduate_school;
    @Column(nullable = false) private String department;
    @Column(nullable = false) private String year;
    @Column(nullable = false) private String semester;
    @Column private String credit;
    @Column(nullable = false) private int COUNT_comments;

    // Timetable와의 N:N 관계 추가
    @ManyToMany(mappedBy = "courses") // Timetable에서 설정한 필드명
    private List<Timetable> timetables = new ArrayList<>();
}
