package com.example.Devkor_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Timetable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(nullable = false)
    private String name; // 시간표 이름 (예: 25-1학기)

    // N:N 관계 - Course
    @ManyToMany
    @JoinTable(
            name = "course_timetable", // 연결 테이블 이름
            joinColumns = @JoinColumn(name = "timetable_id"), // Timetable의 FK
            inverseJoinColumns = @JoinColumn(name = "course_id") // Course의 FK
    )
    private List<Course> courses = new ArrayList<>();

    // N:N 관계 - Privacy
    @ManyToMany
    @JoinTable(
            name = "privacy_timetable", // 연결 테이블 이름
            joinColumns = @JoinColumn(name = "timetable_id"), // Timetable의 FK
            inverseJoinColumns = @JoinColumn(name = "privacy_id") // Privacy의 FK
    )
    private List<Privacy> privacies = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}
