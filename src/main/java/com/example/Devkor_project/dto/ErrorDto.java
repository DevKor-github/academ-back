package com.example.Devkor_project.dto;

import com.example.Devkor_project.configuration.VersionProvider;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class ErrorDto
{
    @Builder.Default
    private String status = "error";

    private String data;

    private String message;

    private String version;
}
