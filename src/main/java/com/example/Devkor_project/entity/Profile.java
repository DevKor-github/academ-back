package com.example.Devkor_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Profile
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profile_id;

    @Column(nullable = false) private String email;
    @Column(nullable = false) private String password;
    @Column(nullable = false) private String username;
    @Column(nullable = false) private String student_id;
    @Column(nullable = false) private String degree;
    @Column(nullable = false) private int semester;
    @Column(nullable = false) private String department;
    @Column(nullable = false) private int point;
    @Column(nullable = false) private LocalDate created_at;
    @Column(nullable = false) private String role;
}
