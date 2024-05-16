package com.example.Devkor_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Code
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code_id;

    @Column(nullable = false) private String email;
    @Column(nullable = false) private String code;
    @Column(nullable = false) private LocalDate created_at;
}
