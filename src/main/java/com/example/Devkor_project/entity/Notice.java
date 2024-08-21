package com.example.Devkor_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Notice
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notice_id;

    @Column(nullable = false) private String title;
    @Column(columnDefinition = "TEXT", nullable = false) private String detail;
    @Column(nullable = false) private LocalDate created_at;

    @Column private String image_1;
    @Column private String image_2;
    @Column private String image_3;
    @Column private String image_4;
    @Column private String image_5;

}
