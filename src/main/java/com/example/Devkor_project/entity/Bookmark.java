package com.example.Devkor_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Bookmark
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmark_id;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile_id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course_id;
}
