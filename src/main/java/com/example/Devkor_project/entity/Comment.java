package com.example.Devkor_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentRating_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CommentRating commentRating_id;

    @Column(columnDefinition = "TEXT", nullable = false) private String review;
    @Column(nullable = false) private int likes;
    @Column(nullable = false) private LocalDate created_at;
    @Column(nullable = false) private LocalDate updated_at;
    @Column(nullable = false) private boolean reward;
}
