package com.example.Academ.dto.login;

import com.example.Academ.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class SignUpRequestDto
{
    private String email;
    private String password;
    private String username;
    private String studentId;
    private int grade;
    private int semester;
    private String department;

    public Profile toEntity()
    {
        return Profile.builder()
                .email(email)
                .password(password)
                .username(username)
                .studentId(studentId)
                .grade(grade)
                .semester(semester)
                .department(department)
                .build();
    };
}
