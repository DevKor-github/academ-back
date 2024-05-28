package com.example.Devkor_project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

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
    }
}
