package com.example.Devkor_project.dto;

import com.example.Devkor_project.entity.Course;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CourseDto
{
    @NotNull(message = "course_id는 null일 수 없습니다.")
    private Long course_id;

    @NotBlank(message = "course_code는 빈 문자열일 수 없습니다.")
    private String course_code;

    @NotBlank(message = "graduate_school은 빈 문자열일 수 없습니다.")
    private String graduate_school;

    @NotBlank(message = "department는 빈 문자열일 수 없습니다.")
    private String department;

    @NotBlank(message = "year은 빈 문자열일 수 없습니다.")
    private String year;

    @NotBlank(message = "semester은 빈 문자열일 수 없습니다.")
    private String semester;

    @NotBlank(message = "name은 빈 문자열일 수 없습니다.")
    private String name;

    @NotBlank(message = "professor은 빈 문자열일 수 없습니다.")
    private String professor;

    private String credit;
    private String time_location;

    @NotNull(message = "rating은 null일 수 없습니다.")
    private double rating;

    @NotNull(message = "amount_of_studying은 null일 수 없습니다.")
    private double amount_of_studying;

    @NotNull(message = "difficulty은 null일 수 없습니다.")
    private double difficulty;

    @NotNull(message = "delivery_power은 null일 수 없습니다.")
    private double delivery_power;

    @NotNull(message = "grading은 null일 수 없습니다.")
    private double grading;

    public static CourseDto CourseToCourseDto(Course course)
    {
        return new CourseDto(
                course.getCourse_id(),
                course.getCourse_code(),
                course.getGraduate_school(),
                course.getDepartment(),
                course.getYear(),
                course.getSemester(),
                course.getName(),
                course.getProfessor(),
                course.getCredit(),
                course.getTime_location(),
                course.getRating(),
                course.getAmount_of_studying(),
                course.getDifficulty(),
                course.getDelivery_power(),
                course.getGrading()
        );
    }
}
