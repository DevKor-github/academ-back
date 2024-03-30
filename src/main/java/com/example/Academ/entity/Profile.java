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
    private Long id;

    @Column private String email;
    @Column private String password;
    @Column private String username;
    @Column private String studentId;
    @Column private int grade;
    @Column private int semester;
    @Column private String department;
}
