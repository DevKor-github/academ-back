package com.example.Devkor_project.dto;

import com.example.Devkor_project.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "profile_id")
        private Long profile_id;

        @NotBlank(message = "[email] cannot be blank.")
        @Email(message = "[email] should be email format.")
        @Schema(description = "이메일")
        private String email;

        @NotBlank(message = "[password] cannot be blank.")
        @Schema(description = "비밀번호")
        private String password;

        @NotBlank(message = "[username] cannot be blank.")
        @Schema(description = "닉네임")
        private String username;

        @NotBlank(message = "[student_id] cannot be blank.")
        @Schema(description = "학번")
        private String student_id;

        @NotBlank(message = "[degree] cannot be blank.")
        @Schema(description = "학위")
        private String degree;

        @NotNull(message = "[semester] cannot be null.")
        @Schema(description = "학기")
        private int semester;

        @NotBlank(message = "[department] cannot be blank.")
        @Schema(description = "학과")
        private String department;

        @NotNull(message = "[point] cannot be null.")
        @Schema(description = "포인트")
        private int point;

        @NotNull(message = "[created_at] cannot be null.")
        @Schema(description = "생성 날짜")
        private LocalDate created_at;

        @NotBlank(message = "[role] cannot be blank.")
        @Schema(description = "권한")
        private String role;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    @Builder
    public static class Signup
    {
        @NotBlank(message = "[email] cannot be blank.")
        @Email(message = "[email] should be email format.")
        @Schema(description = "이메일")
        private String email;

        @NotBlank(message = "[password] cannot be blank.")
        @Schema(description = "비밀번호")
        private String password;

        @NotBlank(message = "[username] cannot be blank.")
        @Schema(description = "닉네임")
        private String username;

        @NotBlank(message = "[student_id] cannot be blank.")
        @Schema(description = "학번")
        private String student_id;

        @NotBlank(message = "[degree] cannot be blank.")
        @Schema(description = "학위")
        private String degree;

        @NotNull(message = "[semester] cannot be null.")
        @Schema(description = "학기")
        private int semester;

        @NotBlank(message = "[department] cannot be blank.")
        @Schema(description = "학과")
        private String department;

        @NotBlank(message = "[code] cannot be blank.")
        @Schema(description = "인증번호")
        private String code;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class ResetPassword
    {
        @NotBlank(message = "[email] cannot be blank.")
        @Email(message = "[email] should be email format.")
        @Schema(description = "이메일")
        private String email;

        @NotBlank(message = "[code] cannot be blank.")
        @Schema(description = "인증번호")
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
        @Schema(description = "profile_id")
        private Long profile_id;

        @NotBlank(message = "[email] cannot be blank.")
        @Email(message = "[email] should be email format.")
        @Schema(description = "이메일")
        private String email;

        @NotBlank(message = "[role] cannot be blank.")
        @Schema(description = "권한")
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
        @Schema(description = "profile_id")
        private Long profile_id;

        @NotBlank(message = "[email] cannot be blank.")
        @Email(message = "[email] should be email format.")
        @Schema(description = "이메일")
        private String email;

        @NotBlank(message = "[username] cannot be blank.")
        @Schema(description = "닉네임")
        private String username;

        @NotBlank(message = "[student_id] cannot be blank.")
        @Schema(description = "학번")
        private String student_id;

        @NotBlank(message = "[degree] cannot be blank.")
        @Schema(description = "학위")
        private String degree;

        @NotNull(message = "[semester] cannot be null.")
        @Schema(description = "학기")
        private int semester;

        @NotBlank(message = "[department] cannot be blank.")
        @Schema(description = "학과")
        private String department;

        @NotNull(message = "[point] cannot be null.")
        @Schema(description = "포인트")
        private int point;

        @NotNull(message = "[access_expiration_date] cannot be null.")
        @Schema(description = "강의평 열람권 만료 날짜")
        private LocalDate access_expiration_date;

        @NotNull(message = "[created_at] cannot be null.")
        @Schema(description = "생성 날짜")
        private LocalDate created_at;

        @NotBlank(message = "[role] cannot be blank.")
        @Schema(description = "권한")
        private String role;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class UpdateBasic
    {
        @NotBlank(message = "[username] cannot be blank.")
        @Schema(description = "닉네임")
        private String username;

        @NotBlank(message = "[student_id] cannot be blank.")
        @Schema(description = "학번")
        private String student_id;

        @NotBlank(message = "[degree] cannot be blank.")
        @Schema(description = "학위")
        private String degree;

        @NotNull(message = "[semester] cannot be null.")
        @Schema(description = "학기")
        private int semester;

        @NotBlank(message = "[department] cannot be blank.")
        @Schema(description = "학과")
        private String department;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class UpdatePassword
    {
        @NotBlank(message = "[old_password] cannot be blank.")
        @Schema(description = "기존 비밀번호")
        private String old_password;

        @NotBlank(message = "[new_password] cannot be blank.")
        @Schema(description = "새로운 비밀번호")
        private String new_password;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class Delete
    {
        @NotBlank(message = "[password] cannot be blank.")
        @Schema(description = "비밀번호")
        private String password;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Builder
    public static class BuyAccessAuth
    {
        @NotBlank(message = "[item] cannot be blank.")
        @Schema(description = "구매 상품")
        private String item;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    public static class CreateTestAccount
    {
        @NotBlank(message = "[email] cannot be blank.")
        @Email(message = "[email] should be email format.")
        @Schema(description = "이메일")
        private String email;

        @NotBlank(message = "[password] cannot be blank.")
        @Schema(description = "비밀번호")
        private String password;
    }
}
