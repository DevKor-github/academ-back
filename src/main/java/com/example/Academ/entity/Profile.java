package com.example.Academ.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Profile
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false) private String email;
    @Column(nullable = false) private String password;
    @Column(nullable = false) private String username;
    @Column(nullable = false) private String studentId;
    @Column(nullable = false) private int grade;
    @Column(nullable = false) private int semester;
    @Column(nullable = false) private String department;
}
