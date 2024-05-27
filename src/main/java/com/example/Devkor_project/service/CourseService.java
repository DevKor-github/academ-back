package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.CommentDto;
import com.example.Devkor_project.dto.CourseDetailDto;
import com.example.Devkor_project.dto.InsertCommentRequestDto;
import com.example.Devkor_project.entity.Bookmark;
import com.example.Devkor_project.entity.Comment;
import com.example.Devkor_project.entity.Course;
import com.example.Devkor_project.entity.Profile;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.BookmarkRepository;
import com.example.Devkor_project.repository.CommentRepository;
import com.example.Devkor_project.repository.CourseRepository;
import com.example.Devkor_project.repository.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseService
{
    @Autowired CourseRepository courseRepository;
    @Autowired ProfileRepository profileRepository;
    @Autowired BookmarkRepository bookmarkRepository;
    @Autowired CommentRepository commentRepository;

    /* 강의 검색 서비스 */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> searchCourse(String keyword)
    {
        // 검색어가 2글자 미만이면 예외 발생
        if(keyword.length() < 2)
            throw new AppException(ErrorCode.SHORT_SEARCH_WORD, keyword);

        // 강의명 + 교수명 + 학수번호 검색
        List<Course> courses = courseRepository.searchCourse(keyword);

        // 검색 결과가 없다면 예외 발생
        if(courses.isEmpty())
            throw new AppException(ErrorCode.NO_RESULT, keyword);

        // entity를 HashMap으로 변경
        List<Map<String, Object>> processedCourses = new ArrayList<>();;
        for(int i = 0; i < courses.size(); i++)
        {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> courseMap = objectMapper.convertValue(courses.get(i), Map.class);
            Map<String, Object> courseProcessedMap = new HashMap<String, Object>();

            for (Map.Entry<String, Object> entry : courseMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                courseProcessedMap.put(key, value);
            }

            processedCourses.add(courseProcessedMap);
        }

        return processedCourses;
    }

    /* 강의 상세 정보 서비스 */
    public CourseDetailDto courseDetail(Long course_id)
    {
        // 사용자가 상세 정보를 요청한 강의가 존재하지 않으면 예외 처리
        Course course = courseRepository.findById(course_id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, course_id));

        // 해당 강의의 강의평들 검색
        List<Comment> comments = commentRepository.findByCourseId(course_id);

        // 강의평 엔티티 리스트를 강의평 dto 리스트로 변환
        List<CommentDto> commentDtos = comments.stream()
                .map(comment -> {

                    Profile profile = profileRepository.findById(comment.getProfile_id().getProfile_id())
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, comment.getProfile_id().getProfile_id()));
                    String username = profile.getUsername();

                    return CommentDto.builder()
                            .comment_id(comment.getComment_id())
                            .profile_username(username)
                            .rating(comment.getRating())
                            .r1_amount_of_studying(comment.getR1_amount_of_studying())
                            .r2_difficulty(comment.getR2_difficulty())
                            .r3_delivery_power(comment.getR3_delivery_power())
                            .r4_grading(comment.getR4_grading())
                            .review(comment.getReview())
                            .teach_t1_theory(comment.isTeach_t1_theory())
                            .teach_t2_practice(comment.isTeach_t2_practice())
                            .teach_t3_seminar(comment.isTeach_t3_seminar())
                            .teach_t4_discussion(comment.isTeach_t4_discussion())
                            .teach_t5_presentation(comment.isTeach_t5_presentation())
                            .learn_t1_theory(comment.isLearn_t1_theory())
                            .learn_t2_thesis(comment.isLearn_t2_thesis())
                            .learn_t3_exam(comment.isLearn_t3_exam())
                            .learn_t4_industry(comment.isLearn_t4_industry())
                            .likes(comment.getLikes())
                            .created_at(comment.getCreated_at())
                            .updated_at(comment.getUpdated_at())
                            .build();

                })
                .toList();

        // course 엔티티와 강의평 dto 리스트로 CourseDetailDto 만들어서 반환
        return CourseDetailDto.makeCourseDetailDto(course, commentDtos);
    }

    /* 강의 북마크 서비스 */
    @Transactional
    public void bookmark(Principal principal, Long course_id)
    {
        // 북마크 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 북마크 요청을 보낸 사용자의 계정이 존재하지 않으면 예외 처리
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // 사용자가 북마크할 강의가 존재하지 않으면 예외 처리
        Course course = courseRepository.findById(course_id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, course_id));

        // 해당 북마크가 정보가 현재 존재하지 않으면, 북마크 생성
        // 해당 북마크가 정보가 이미 존재하면, 북마크 해제
        List<Bookmark> bookmark = bookmarkRepository.searchBookmark(profile.getProfile_id(), course.getCourse_id());

        if(bookmark.isEmpty())
        {
            // Bookmark entity 생성
            Bookmark newBookmark = Bookmark.builder()
                    .profile_id(profile)
                    .course_id(course)
                    .build();

            bookmarkRepository.save(newBookmark);
        }
        else
            bookmarkRepository.delete(bookmark.getFirst());

    }

    /* 강의평 작성 시작 서비스 */
    public void startComment(Principal principal, Long course_id)
    {
        // 강의평 작성 시작 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 강의평 작성 시작 요청을 보낸 사용자의 계정이 존재하지 않으면 예외 처리
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // 사용자가 강의평을 추가할 강의가 존재하지 않으면 예외 처리
        Course course = courseRepository.findById(course_id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, course_id));

        // 해당 사용자가 이미 해당 강의에 강의평을 달았다면, 예외 처리
        List<Comment> comment = commentRepository.searchComment(profile.getProfile_id(), course_id);
        if(!comment.isEmpty())
            throw new AppException(ErrorCode.ALREADY_EXIST, course_id);

    }

    /* 강의평 작성 완료 및 등록 서비스 */
    @Transactional
    public void insertComment(Principal principal, InsertCommentRequestDto dto)
    {
        // 강의평 작성 시작 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 강의평 작성 시작 요청을 보낸 사용자의 계정이 존재하지 않으면 예외 처리
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // 사용자가 강의평을 추가할 강의가 존재하지 않으면 예외 처리
        Course course = courseRepository.findById(dto.getCourse_id())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, dto.getCourse_id()));

        // 해당 사용자가 이미 해당 강의에 강의평을 달았다면, 예외 처리
        List<Comment> comment = commentRepository.searchComment(profile.getProfile_id(), course.getCourse_id());
        if(!comment.isEmpty())
            throw new AppException(ErrorCode.ALREADY_EXIST, course.getCourse_id());

        // 강의평 추가
        Comment newComment = Comment.builder()
                .profile_id(profile)
                .course_id(course)
                .rating(dto.getRating())
                .r1_amount_of_studying(dto.getR1_amount_of_studying())
                .r2_difficulty(dto.getR2_difficulty())
                .r3_delivery_power(dto.getR3_delivery_power())
                .r4_grading(dto.getR4_grading())
                .review(dto.getReview())
                .teach_t1_theory(dto.isTeach_t1_theory())
                .teach_t2_practice(dto.isTeach_t2_practice())
                .teach_t3_seminar(dto.isTeach_t3_seminar())
                .teach_t4_discussion(dto.isTeach_t4_discussion())
                .teach_t5_presentation(dto.isTeach_t5_presentation())
                .learn_t1_theory(dto.isLearn_t1_theory())
                .learn_t2_thesis(dto.isLearn_t2_thesis())
                .learn_t3_exam(dto.isLearn_t3_exam())
                .learn_t4_industry(dto.isLearn_t4_industry())
                .likes(0)
                .created_at(LocalDate.now())
                .updated_at(LocalDate.now())
                .build();

        commentRepository.save(newComment);

        // 강의 엔티티의 강의평 개수 업데이트
        int count = course.getCOUNT_comments() + 1;
        course.setCOUNT_comments(count);

        // 강의 엔티티의 평점 평균값 업데이트
        course.setAVG_rating((course.getAVG_rating() * (count - 1) + dto.getRating()) / count);
        course.setAVG_r1_amount_of_studying((course.getAVG_r1_amount_of_studying() * (count - 1) + dto.getR1_amount_of_studying()) / count);
        course.setAVG_r2_difficulty((course.getAVG_r2_difficulty() * (count - 1) + dto.getR2_difficulty()) / count);
        course.setAVG_r3_delivery_power((course.getAVG_r3_delivery_power() * (count - 1) + dto.getR3_delivery_power()) / count);
        course.setAVG_r4_grading((course.getAVG_r4_grading() * (count - 1) + dto.getR4_grading()) / count);

        // 강의 엔티티의 태그 개수 업데이트
        if(dto.isTeach_t1_theory())
            course.setCOUNT_teach_t1_theory(course.getCOUNT_teach_t1_theory() + 1);
        if(dto.isTeach_t2_practice())
            course.setCOUNT_teach_t2_practice(course.getCOUNT_teach_t2_practice() + 1);
        if(dto.isTeach_t3_seminar())
            course.setCOUNT_teach_t3_seminar(course.getCOUNT_teach_t3_seminar() + 1);
        if(dto.isTeach_t4_discussion())
            course.setCOUNT_teach_t4_discussion(course.getCOUNT_teach_t4_discussion() + 1);
        if(dto.isTeach_t5_presentation())
            course.setCOUNT_teach_t5_presentation(course.getCOUNT_teach_t5_presentation() + 1);
        if(dto.isLearn_t1_theory())
            course.setCOUNT_learn_t1_theory(course.getCOUNT_learn_t1_theory() + 1);
        if(dto.isLearn_t2_thesis())
            course.setCOUNT_learn_t2_thesis(course.getCOUNT_learn_t2_thesis() + 1);
        if(dto.isLearn_t3_exam())
            course.setCOUNT_learn_t3_exam(course.getCOUNT_learn_t3_exam() + 1);
        if(dto.isLearn_t4_industry())
            course.setCOUNT_learn_t4_industry(course.getCOUNT_learn_t4_industry() + 1);

        courseRepository.save(course);
    }
}
