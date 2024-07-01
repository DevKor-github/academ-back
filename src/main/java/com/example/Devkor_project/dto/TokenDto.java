package com.example.Devkor_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class TokenDto {
    private String accessToken;
    private String refreshToken;
}
