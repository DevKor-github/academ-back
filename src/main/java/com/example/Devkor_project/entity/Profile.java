package com.example.Devkor_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Profile
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Column(nullable = false) private String email;
    @Column(nullable = false) private String password;
    @Column(nullable = false) private String username;
    @Column(nullable = false) private String studentId;
    @Column(nullable = false) private int grade;
    @Column(nullable = false) private int semester;
    @Column(nullable = false) private String department;
    @Column(nullable = false) private int point;

    @Column(nullable = false) private String role;
}
