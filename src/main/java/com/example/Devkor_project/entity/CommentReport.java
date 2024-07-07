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
public class CommentReport
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentReport_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment comment_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Profile profile_id;

    @Column(nullable = false) private String reason;
    @Column(columnDefinition = "TEXT", nullable = true) private String detail;
    @Column(nullable = false) private LocalDate created_at;
}
