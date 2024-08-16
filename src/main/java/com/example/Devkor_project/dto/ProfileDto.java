package com.example.Devkor_project.dto;

import com.example.Devkor_project.entity.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class ProfileDto
{
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class Profile
    {
        @NotNull(message = "[profile_id] cannot be null.")
        private Long profile_id;

        @NotBlank(message = "[email] cannot be blank.")
        @Email(message = "[email] should be email format.")
        private String email;

        @NotBlank(message = "[password] cannot be blank.")
        private String password;

        @NotBlank(message = "[username] cannot be blank.")
        private String username;

        @NotBlank(message = "[student_id] cannot be blank.")
        private String student_id;

        @NotBlank(message = "[degree] cannot be blank.")
        private String degree;

        @NotNull(message = "[semester] cannot be null.")
        private int semester;

        @NotBlank(message = "[department] cannot be blank.")
        private String department;

        @NotNull(message = "[point] cannot be null.")
        private int point;

        @NotNull(message = "[created_at] cannot be null.")
        private LocalDate created_at;

        @NotBlank(message = "[role] cannot be blank.")
        private String role;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    public static class Signup
    {
        @NotBlank(message = "[email] cannot be blank.")
        @Email(message = "[email] should be email format.")
        private String email;

        @NotBlank(message = "[password] cannot be blank.")
        private String password;

        @NotBlank(message = "[username] cannot be blank.")
        private String username;

        @NotBlank(message = "[student_id] cannot be blank.")
        private String student_id;

        @NotBlank(message = "[degree] cannot be blank.")
        private String degree;

        @NotNull(message = "[semester] cannot be null.")
        private int semester;

        @NotBlank(message = "[department] cannot be blank.")
        private String department;

        @NotBlank(message = "[code] cannot be blank.")
        private String code;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class CheckLogin
    {
        @NotNull(message = "[profile_id] cannot be null.")
        private Long profile_id;

        @NotBlank(message = "[email] cannot be blank.")
        @Email(message = "[email] should be email format.")
        private String email;

        @NotBlank(message = "[username] cannot be blank.")
        private String username;

        @NotBlank(message = "[student_id] cannot be blank.")
        private String student_id;

        @NotBlank(message = "[degree] cannot be blank.")
        private String degree;

        @NotNull(message = "[semester] cannot be null.")
        private int semester;

        @NotBlank(message = "[department] cannot be blank.")
        private String department;

        @NotNull(message = "[point] cannot be null.")
        private int point;

        @NotNull(message = "[access_expiration_date] cannot be null.")
        private LocalDate access_expiration_date;

        @NotNull(message = "[created_at] cannot be null.")
        private LocalDate created_at;

        @NotBlank(message = "[role] cannot be blank.")
        private String role;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class MyPage
    {
        @NotNull(message = "[profile_id] cannot be null.")
        private Long profile_id;

        @NotBlank(message = "[email] cannot be blank.")
        @Email(message = "[email] should be email format.")
        private String email;

        @NotBlank(message = "[username] cannot be blank.")
        private String username;

        @NotBlank(message = "[student_id] cannot be blank.")
        private String student_id;

        @NotBlank(message = "[degree] cannot be blank.")
        private String degree;

        @NotNull(message = "[semester] cannot be null.")
        private int semester;

        @NotBlank(message = "[department] cannot be blank.")
        private String department;

        @NotNull(message = "[point] cannot be null.")
        private int point;

        @NotNull(message = "[access_expiration_date] cannot be null.")
        private LocalDate access_expiration_date;

        @NotNull(message = "[created_at] cannot be null.")
        private LocalDate created_at;

        @NotBlank(message = "[role] cannot be blank.")
        private String role;

        private List<CourseDto.Basic> courses;
        private List<CommentDto.MyPage> comments;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class CheckPassword
    {
        @NotBlank(message = "[password] cannot be blank.")
        private String password;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class UpdateBasic
    {
        @NotBlank(message = "[username] cannot be blank.")
        private String username;

        @NotBlank(message = "[student_id] cannot be blank.")
        private String student_id;

        @NotBlank(message = "[degree] cannot be blank.")
        private String degree;

        @NotNull(message = "[semester] cannot be null.")
        private int semester;

        @NotBlank(message = "[department] cannot be blank.")
        private String department;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class UpdatePassword
    {
        @NotBlank(message = "[oldPassword] cannot be blank.")
        private String oldPassword;

        @NotBlank(message = "[newPassword] cannot be blank.")
        private String newPassword;
    }

    public static ProfileDto.MyPage entityToMyPage(com.example.Devkor_project.entity.Profile profile,
                                                   List<CourseDto.Basic> courseDtos,
                                                   List<CommentDto.MyPage> commentDtos)
    {
        return MyPage.builder()
                .profile_id(profile.getProfile_id())
                .email(profile.getEmail())
                .username(profile.getUsername())
                .student_id(profile.getStudent_id())
                .degree(profile.getDegree())
                .semester(profile.getSemester())
                .department(profile.getDepartment())
                .point(profile.getPoint())
                .access_expiration_date(profile.getAccess_expiration_date())
                .created_at(profile.getCreated_at())
                .role(profile.getRole())
                .courses(courseDtos)
                .comments(commentDtos)
                .build();
    }
}
