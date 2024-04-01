package com.example.Devkor_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class LoginRequestDto
{
    private String email;
    private String password;
    private boolean isSaved;
}
