package com.example.Devkor_project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class EmailCheckRequestDto
{
    @NotBlank(message = "email은 빈 문자열일 수 없습니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "authenticationCode는 빈 문자열일 수 없습니다.")
    private String authenticationCode;
}
