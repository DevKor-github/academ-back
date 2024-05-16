package com.example.Devkor_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class SuccessDto
{
    @Builder.Default
    private String status = "success";

    private String data;

    private String message;

    private String version;
}
