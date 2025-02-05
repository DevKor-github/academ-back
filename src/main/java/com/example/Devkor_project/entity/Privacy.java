package com.example.Devkor_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Privacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 개인 일정 이름

    @Column(nullable = false)
    private String day; // 요일 (e.g., 월, 화, 수)

    @Column(nullable = false)
    private LocalTime startTime; // 시작 시간

    @Column(nullable = false)
    private LocalTime finishTime; // 종료 시간

    private String location; // 장소

    // Timetable과 N:N 관계 설정
    @ManyToMany
    @JoinTable(
            name = "privacy_timetable",
            joinColumns = @JoinColumn(name = "privacy_id"),
            inverseJoinColumns = @JoinColumn(name = "timetable_id")
    )
    private List<Timetable> timetables = new ArrayList<>();

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
    // Builder에 timetables 처리 추가
    public static class PrivacyBuilder {
        private List<Timetable> timetables = new ArrayList<>();

        public PrivacyBuilder timetable(Timetable timetable) {
            this.timetables.add(timetable);
            return this;
        }

        public PrivacyBuilder timetables(List<Timetable> timetables) {
            this.timetables = timetables;
            return this;
        }
    }

}
