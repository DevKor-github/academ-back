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
    private String status = "SUCCESS";

    private Object data;

    private String message;

    private String version;
}
