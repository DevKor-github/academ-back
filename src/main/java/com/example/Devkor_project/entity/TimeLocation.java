package com.example.Devkor_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class TimeLocation
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeLocation_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Course course_id;

    @Column(nullable = true) private String day;
    @Column(nullable = true) private Integer startPeriod;
    @Column(nullable = true) private Integer endPeriod;
    @Column(nullable = true) private String location;
}
