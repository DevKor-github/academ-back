package com.example.Devkor_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class ErrorDto
{
    private String error_code;
    private String description;
}
