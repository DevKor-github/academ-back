package com.example.Devkor_project.dto;

import com.example.Devkor_project.entity.Comment;
import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.repository.ProfileRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class CourseDetailDto
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

    @NotNull(message = "COUNT_comments는 null일 수 없습니다.")
    private int COUNT_comments;
    @NotNull(message = "AVG_rating은 null일 수 없습니다.")
    private double AVG_rating;
    @NotNull(message = "AVG_r1_amount_of_studying은 null일 수 없습니다.")
    private double AVG_r1_amount_of_studying;
    @NotNull(message = "AVG_r2_difficulty는 null일 수 없습니다.")
    private double AVG_r2_difficulty;
    @NotNull(message = "AVG_r3_delivery_power은 null일 수 없습니다.")
    private double AVG_r3_delivery_power;
    @NotNull(message = "AVG_r4_grading은 null일 수 없습니다.")
    private double AVG_r4_grading;

    @NotNull(message = "COUNT_teach_t1_theory는 null일 수 없습니다.")
    private int COUNT_teach_t1_theory;
    @NotNull(message = "COUNT_teach_t2_practice는 null일 수 없습니다.")
    private int COUNT_teach_t2_practice;
    @NotNull(message = "COUNT_teach_t3_seminar는 null일 수 없습니다.")
    private int COUNT_teach_t3_seminar;
    @NotNull(message = "COUNT_teach_t4_discussion는 null일 수 없습니다.")
    private int COUNT_teach_t4_discussion;
    @NotNull(message = "COUNT_teach_t5_presentation는 null일 수 없습니다.")
    private int COUNT_teach_t5_presentation;
    @NotNull(message = "COUNT_learn_t1_theory는 null일 수 없습니다.")
    private int COUNT_learn_t1_theory;
    @NotNull(message = "COUNT_learn_t2_thesis는 null일 수 없습니다.")
    private int COUNT_learn_t2_thesis;
    @NotNull(message = "COUNT_learn_t3_exam는 null일 수 없습니다.")
    private int COUNT_learn_t3_exam;
    @NotNull(message = "COUNT_learn_t4_industry는 null일 수 없습니다.")
    private int COUNT_learn_t4_industry;

    private List<CommentDto> comments;

    public static CourseDetailDto makeCourseDetailDto(Course course, List<CommentDto> commentDtos)
    {
        return new CourseDetailDto(
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
                course.getCOUNT_comments(),
                course.getAVG_rating(),
                course.getAVG_r1_amount_of_studying(),
                course.getAVG_r2_difficulty(),
                course.getAVG_r3_delivery_power(),
                course.getAVG_r4_grading(),
                course.getCOUNT_teach_t1_theory(),
                course.getCOUNT_teach_t2_practice(),
                course.getCOUNT_teach_t3_seminar(),
                course.getCOUNT_teach_t4_discussion(),
                course.getCOUNT_teach_t5_presentation(),
                course.getCOUNT_learn_t1_theory(),
                course.getCOUNT_learn_t2_thesis(),
                course.getCOUNT_learn_t3_exam(),
                course.getCOUNT_learn_t4_industry(),
                commentDtos
        );
    }
}
