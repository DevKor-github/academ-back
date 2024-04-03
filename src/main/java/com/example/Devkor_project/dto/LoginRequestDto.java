package com.example.Devkor_project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class LoginRequestDto
{
    @NotBlank(message = "email은 빈 문자열일 수 없습니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "password는 빈 문자열일 수 없습니다.")
    private String password;

    @NotNull(message = "isSaved는 null일 수 없습니다.")
    private boolean isSaved;
}
