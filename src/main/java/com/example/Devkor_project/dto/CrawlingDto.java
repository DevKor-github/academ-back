package com.example.Devkor_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

public class CrawlingDto
{
    @AllArgsConstructor
    @Getter
    @Builder
    public static class Condition
    {
        @NotBlank(message = "obj는 빈 문자열일 수 없습니다.")
        private String obj;
        @NotBlank(message = "args는 빈 문자열일 수 없습니다.")
        private List<String> args;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class GraduateSchool
    {
        @NotBlank(message = "code는 빈 문자열일 수 없습니다.")
        private String code;
        @NotBlank(message = "name은 빈 문자열일 수 없습니다.")
        private String name;
        @NotNull(message = "rowid는 null일 수 없습니다.")
        private int rowid;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Response_GraduateSchool
    {
        @NotNull(message = "data는 null일 수 없습니다.")
        private List<CrawlingDto.GraduateSchool> data;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Department
    {
        @NotBlank(message = "code는 빈 문자열일 수 없습니다.")
        private String code;
        @NotBlank(message = "name은 빈 문자열일 수 없습니다.")
        private String name;
        @NotNull(message = "rowid는 null일 수 없습니다.")
        private int rowid;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Response_Department
    {
        @NotNull(message = "data는 null일 수 없습니다.")
        private List<CrawlingDto.Department> data;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    @Builder
    public static class Course
    {
        private String lmt_yn;
        private String grad_cd;
        private String year;
        private String wtime;
        private String major_nm;
        private String isu_nm;
        private String cour_cd;
        private String etc;
        private String term;
        private String flexible_school_yn;
        private String ptime;
        private String flexible_term;
        private String grad_nm;
        private String absolute_yn;
        private String time_room;
        private String campus;
        private String flexible_to_dt;
        private String lec111_cour_div;
        private String flexible_fr_dt;
        private String prof_nm;
        private String cour_nm;
        private String dept_cd;
        private String cour_cls;
        private String time;
        private String exch_cor_yn;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Response_Course
    {
        @NotNull(message = "data는 null일 수 없습니다.")
        private List<CrawlingDto.Course> data;
    }
}
