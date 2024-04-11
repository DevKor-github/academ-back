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
    @NotNull(message = "courseId는 null일 수 없습니다.")
    private Long courseId;

    @NotBlank(message = "courseCode는 빈 문자열일 수 없습니다.")
    private String courseCode;

    @NotBlank(message = "graduateSchool은 빈 문자열일 수 없습니다.")
    private String graduateSchool;

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

    @NotBlank(message = "time은 빈 문자열일 수 없습니다.")
    private String time;

    private String location;

    @NotNull(message = "rating은 null일 수 없습니다.")
    private double rating;

    public static CourseDto CourseToCousreDto(Course course)
    {
        return new CourseDto(
                course.getCourseId(),
                course.getCourseCode(),
                course.getGraduateSchool(),
                course.getDepartment(),
                course.getYear(),
                course.getSemester(),
                course.getName(),
                course.getProfessor(),
                course.getTime(),
                course.getLocation(),
                course.getRating()
        );
    }
}
