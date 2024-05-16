package com.example.Devkor_project.dto;

import com.example.Devkor_project.entity.Profile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class SignUpRequestDto
{
    @NotBlank(message = "email은 빈 문자열일 수 없습니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "password는 빈 문자열일 수 없습니다.")
    private String password;

    @NotBlank(message = "username은 빈 문자열일 수 없습니다.")
    private String username;

    @NotBlank(message = "student_id는 빈 문자열일 수 없습니다.")
    private String student_id;

    @NotNull(message = "grade는 null일 수 없습니다.")
    private int grade;

    @NotNull(message = "semester는 null일 수 없습니다.")
    private int semester;

    @NotBlank(message = "department는 빈 문자열일 수 없습니다.")
    private String department;
}
