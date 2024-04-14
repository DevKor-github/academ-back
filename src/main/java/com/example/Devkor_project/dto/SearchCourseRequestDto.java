package com.example.Devkor_project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class SearchCourseRequestDto
{
    @NotBlank(message = "searchMode는 빈 문자열일 수 없습니다.")
    private String searchMode;

    @NotBlank(message = "searchWord는 빈 문자열일 수 없습니다.")
    private String searchWord;
}
