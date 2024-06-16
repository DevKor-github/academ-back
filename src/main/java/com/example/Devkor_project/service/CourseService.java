package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.CommentDto;
import com.example.Devkor_project.dto.CourseDetailDto;
import com.example.Devkor_project.dto.CourseDto;
import com.example.Devkor_project.entity.*;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class CourseService
{
    @Autowired ProfileRepository profileRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseRatingRepository courseRatingRepository;
    @Autowired BookmarkRepository bookmarkRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired CommentRatingRepository commentRatingRepository;

    /* 강의 검색 서비스 */
    public List<CourseDto> searchCourse(String keyword)
    {
        // 검색어가 2글자 미만이면 예외 발생
        if(keyword.length() < 2)
            throw new AppException(ErrorCode.SHORT_SEARCH_WORD, keyword);

        // 강의명+교수명+학수번호 검색 후, Course 엔티티 리스트 생성
        List<Course> courses = courseRepository.searchCourse(keyword);

        // 검색 결과가 없다면 예외 발생
        if(courses.isEmpty())
            throw new AppException(ErrorCode.NO_RESULT, keyword);

        // Course 엔티티 리스트 -> courseDto 리스트
        List<CourseDto> courseDtos = courses.stream()
                .map(course -> {

                    // 해당 강의의 평점 데이터가 존재하지 않으면 예외 처리
                    CourseRating courseRating = courseRatingRepository.findById(course.getCourseRating_id().getCourseRating_id())
                            .orElseThrow(() -> new AppException(ErrorCode.COURSE_RATING_NOT_FOUND, course.getCourseRating_id().getCourseRating_id()));

                    return CourseDto.builder()
                            .course_id(course.getCourse_id())
                            .course_code(course.getCourse_code())
                            .graduate_school(course.getCourse_code())
                            .department(course.getDepartment())
                            .year(course.getYear())
                            .semester(course.getSemester())
                            .name(course.getName())
                            .professor(course.getProfessor())
                            .credit(course.getCredit())
                            .time_location(course.getTime_location())
                            .COUNT_comments(course.getCOUNT_comments())
                            .AVG_rating(courseRating.getAVG_rating())
                            .AVG_r1_amount_of_studying(courseRating.getAVG_r1_amount_of_studying())
                            .AVG_r2_difficulty(courseRating.getAVG_r2_difficulty())
                            .AVG_r3_delivery_power(courseRating.getAVG_r3_delivery_power())
                            .AVG_r4_grading(courseRating.getAVG_r4_grading())
                            .COUNT_teach_t1_theory(courseRating.getCOUNT_teach_t1_theory())
                            .COUNT_teach_t2_practice(courseRating.getCOUNT_teach_t2_practice())
                            .COUNT_teach_t3_seminar(courseRating.getCOUNT_teach_t3_seminar())
                            .COUNT_teach_t4_discussion(courseRating.getCOUNT_teach_t4_discussion())
                            .COUNT_teach_t5_presentation(courseRating.getCOUNT_teach_t5_presentation())
                            .COUNT_learn_t1_theory(courseRating.getCOUNT_learn_t1_theory())
                            .COUNT_learn_t2_thesis(courseRating.getCOUNT_learn_t2_thesis())
                            .COUNT_learn_t3_exam(courseRating.getCOUNT_learn_t3_exam())
                            .COUNT_learn_t4_industry(courseRating.getCOUNT_learn_t4_industry())
                            .build();
                })
                .toList();

        return courseDtos;
    }

    /* 강의 상세 정보 서비스 */
    public CourseDetailDto courseDetail(Long course_id)
    {
        // 사용자가 상세 정보를 요청한 강의가 존재하지 않으면 예외 처리
        Course course = courseRepository.findById(course_id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, course_id));

        // 해당 강의의 평점 데이터가 존재하지 않으면 예외 처리
        CourseRating courseRating = courseRatingRepository.findById(course.getCourseRating_id().getCourseRating_id())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_RATING_NOT_FOUND, course.getCourseRating_id().getCourseRating_id()));

        // 해당 강의의 강의평들 검색
        List<Comment> comments = commentRepository.findByCourseId(course_id);

        // 강의평 엔티티 리스트를 강의평 dto 리스트로 변환
        List<CommentDto.Detail> commentDtos = comments.stream()
                .map(comment -> {

                    // 해당 강의평을 작성한 사용자의 profile_id가 존재하지 않으면 예외 처리
                    Profile profile = profileRepository.findById(comment.getProfile_id().getProfile_id())
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, comment.getProfile_id().getProfile_id()));

                    // 해당 강의평의 평점 데이터가 존재하지 않으면 예외 처리
                    CommentRating commentRating = commentRatingRepository.findById(comment.getCommentRating_id().getCommentRating_id())
                            .orElseThrow(() -> new AppException(ErrorCode.COMMENT_RATING_NOT_FOUND, comment.getCommentRating_id().getCommentRating_id()));

                    return CommentDto.Detail.builder()
                            .comment_id(comment.getComment_id())
                            .profile_id(profile.getProfile_id())
                            .username(profile.getUsername())
                            .rating(commentRating.getRating())
                            .r1_amount_of_studying(commentRating.getR1_amount_of_studying())
                            .r2_difficulty(commentRating.getR2_difficulty())
                            .r3_delivery_power(commentRating.getR3_delivery_power())
                            .r4_grading(commentRating.getR4_grading())
                            .review(comment.getReview())
                            .teach_t1_theory(commentRating.isTeach_t1_theory())
                            .teach_t2_practice(commentRating.isTeach_t2_practice())
                            .teach_t3_seminar(commentRating.isTeach_t3_seminar())
                            .teach_t4_discussion(commentRating.isTeach_t4_discussion())
                            .teach_t5_presentation(commentRating.isTeach_t5_presentation())
                            .learn_t1_theory(commentRating.isLearn_t1_theory())
                            .learn_t2_thesis(commentRating.isLearn_t2_thesis())
                            .learn_t3_exam(commentRating.isLearn_t3_exam())
                            .learn_t4_industry(commentRating.isLearn_t4_industry())
                            .likes(comment.getLikes())
                            .created_at(comment.getCreated_at())
                            .updated_at(comment.getUpdated_at())
                            .build();

                })
                .toList();

        // course 엔티티와 강의평 dto 리스트로 CourseDetailDto 만들어서 반환
        return CourseDetailDto.makeCourseDetailDto(course, courseRating, commentDtos);
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
    public void startInsertComment(Principal principal, Long course_id)
    {
        // 강의평 작성 시작 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 강의평 작성 시작 요청을 보낸 사용자의 계정이 존재하지 않으면, 예외 처리
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // 사용자가 강의평을 추가할 강의가 존재하지 않으면, 예외 처리
        Course course = courseRepository.findById(course_id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, course_id));

        // 해당 사용자가 이미 해당 강의에 강의평을 달았다면, 예외 처리
        List<Comment> comment = commentRepository.searchComment(profile.getProfile_id(), course_id);
        if(!comment.isEmpty())
            throw new AppException(ErrorCode.ALREADY_EXIST, course_id);

    }

    /* 강의평 작성 완료 및 등록 서비스 */
    @Transactional
    public void insertComment(Principal principal, CommentDto.Insert dto)
    {
        // 강의평 작성 시작 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 강의평 작성 시작 요청을 보낸 사용자의 계정이 존재하지 않으면 예외 처리
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // 사용자가 강의평을 추가할 강의가 존재하지 않으면 예외 처리
        Course course = courseRepository.findById(dto.getCourse_id())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, dto.getCourse_id()));

        // 해당 강의의 평점 데이터가 존재하지 않으면 예외 처리
        CourseRating courseRating = courseRatingRepository.findById(course.getCourseRating_id().getCourseRating_id())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_RATING_NOT_FOUND, course.getCourseRating_id().getCourseRating_id()));

        // 해당 사용자가 이미 해당 강의에 강의평을 달았다면, 예외 처리
        List<Comment> comment = commentRepository.searchComment(profile.getProfile_id(), course.getCourse_id());
        if(!comment.isEmpty())
            throw new AppException(ErrorCode.ALREADY_EXIST, course.getCourse_id());

        // 새로운 강의평 평점 엔티티 생성 후, 저장
        CommentRating newCommentRating = CommentRating.builder()
                .rating(dto.getRating())
                .r1_amount_of_studying(dto.getR1_amount_of_studying())
                .r2_difficulty(dto.getR2_difficulty())
                .r3_delivery_power(dto.getR3_delivery_power())
                .r4_grading(dto.getR4_grading())
                .teach_t1_theory(dto.isTeach_t1_theory())
                .teach_t2_practice(dto.isTeach_t2_practice())
                .teach_t3_seminar(dto.isTeach_t3_seminar())
                .teach_t4_discussion(dto.isTeach_t4_discussion())
                .teach_t5_presentation(dto.isTeach_t5_presentation())
                .learn_t1_theory(dto.isLearn_t1_theory())
                .learn_t2_thesis(dto.isLearn_t2_thesis())
                .learn_t3_exam(dto.isLearn_t3_exam())
                .learn_t4_industry(dto.isLearn_t4_industry())
                .build();

        commentRatingRepository.save(newCommentRating);

        // 새로운 강의평 엔티티 생성
        Comment newComment = Comment.builder()
                .profile_id(profile)
                .course_id(course)
                .commentRating_id(newCommentRating)
                .review(dto.getReview())
                .likes(0)
                .created_at(LocalDate.now())
                .updated_at(LocalDate.now())
                .reward(false)
                .build();

        // 강의 엔티티의 강의평 개수 업데이트
        int count = course.getCOUNT_comments() + 1;
        course.setCOUNT_comments(count);

        // 강의 평점 엔티티의 평점 평균값 업데이트
        courseRating.setAVG_rating((courseRating.getAVG_rating() * (count - 1) + dto.getRating()) / count);
        courseRating.setAVG_r1_amount_of_studying((courseRating.getAVG_r1_amount_of_studying() * (count - 1) + dto.getR1_amount_of_studying()) / count);
        courseRating.setAVG_r2_difficulty((courseRating.getAVG_r2_difficulty() * (count - 1) + dto.getR2_difficulty()) / count);
        courseRating.setAVG_r3_delivery_power((courseRating.getAVG_r3_delivery_power() * (count - 1) + dto.getR3_delivery_power()) / count);
        courseRating.setAVG_r4_grading((courseRating.getAVG_r4_grading() * (count - 1) + dto.getR4_grading()) / count);

        // 강의 평점 엔티티의 태그 개수 업데이트
        if(dto.isTeach_t1_theory())
            courseRating.setCOUNT_teach_t1_theory(courseRating.getCOUNT_teach_t1_theory() + 1);
        if(dto.isTeach_t2_practice())
            courseRating.setCOUNT_teach_t2_practice(courseRating.getCOUNT_teach_t2_practice() + 1);
        if(dto.isTeach_t3_seminar())
            courseRating.setCOUNT_teach_t3_seminar(courseRating.getCOUNT_teach_t3_seminar() + 1);
        if(dto.isTeach_t4_discussion())
            courseRating.setCOUNT_teach_t4_discussion(courseRating.getCOUNT_teach_t4_discussion() + 1);
        if(dto.isTeach_t5_presentation())
            courseRating.setCOUNT_teach_t5_presentation(courseRating.getCOUNT_teach_t5_presentation() + 1);
        if(dto.isLearn_t1_theory())
            courseRating.setCOUNT_learn_t1_theory(courseRating.getCOUNT_learn_t1_theory() + 1);
        if(dto.isLearn_t2_thesis())
            courseRating.setCOUNT_learn_t2_thesis(courseRating.getCOUNT_learn_t2_thesis() + 1);
        if(dto.isLearn_t3_exam())
            courseRating.setCOUNT_learn_t3_exam(courseRating.getCOUNT_learn_t3_exam() + 1);
        if(dto.isLearn_t4_industry())
            courseRating.setCOUNT_learn_t4_industry(courseRating.getCOUNT_learn_t4_industry() + 1);

        // 해당 사용자에게 100 포인트 지급
        profile.setPoint(profile.getPoint() + 100);

        // 변경 사항 적용
        profileRepository.save(profile);
        courseRepository.save(course);
        courseRatingRepository.save(courseRating);
        commentRepository.save(newComment);
    }

    /* 강의평 수정 시작 서비스 */
    public CommentDto.Update startUpdateComment(Principal principal, Long comment_id)
    {
        // 강의평 작성 시작 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 강의평 작성 시작 요청을 보낸 사용자의 계정이 존재하지 않으면, 예외 처리
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // 사용자가 수정할 강의평이 존재하지 않으면, 예외 처리
        Comment comment = commentRepository.findById(comment_id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND, comment_id));

        // 해당 강의평이 해당 사용자가 작성한 강의평이 아니라면, 예외 처리
        if(!Objects.equals(comment.getProfile_id().getProfile_id(), profile.getProfile_id()))
            throw new AppException(ErrorCode.NOT_COMMENT_BY_USER, comment.getProfile_id().getProfile_id());

        CommentRating commentRating = comment.getCommentRating_id();

        return CommentDto.Update.builder()
                .comment_id(comment_id)
                .rating(commentRating.getRating())
                .r1_amount_of_studying(commentRating.getR1_amount_of_studying())
                .r2_difficulty(commentRating.getR2_difficulty())
                .r3_delivery_power(commentRating.getR3_delivery_power())
                .r4_grading(commentRating.getR4_grading())
                .review(comment.getReview())
                .teach_t1_theory(commentRating.isTeach_t1_theory())
                .teach_t2_practice(commentRating.isTeach_t2_practice())
                .teach_t3_seminar(commentRating.isTeach_t3_seminar())
                .teach_t4_discussion(commentRating.isTeach_t4_discussion())
                .teach_t5_presentation(commentRating.isTeach_t5_presentation())
                .learn_t1_theory(commentRating.isLearn_t1_theory())
                .learn_t2_thesis(commentRating.isLearn_t2_thesis())
                .learn_t3_exam(commentRating.isLearn_t3_exam())
                .learn_t4_industry(commentRating.isLearn_t4_industry())
                .build();
    }

    /* 강의평 수정 완료 서비스 */
    @Transactional
    public Long updateComment(Principal principal, CommentDto.Update dto)
    {
        // 강의평 작성 수정 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 강의평 작성 수정 요청을 보낸 사용자의 계정이 존재하지 않으면 예외 처리
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // 사용자가 수정할 강의평이 존재하지 않으면 예외 처리
        Comment comment = commentRepository.findById(dto.getComment_id())
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND, dto.getComment_id()));

        // 사용자가 강의평을 수정할 강의가 존재하지 않으면 예외 처리
        Course course = courseRepository.findById(comment.getCourse_id().getCourse_id())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, comment.getCourse_id().getCourse_id()));

        // 해당 강의평의 평점 데이터
        CommentRating commentRating = comment.getCommentRating_id();

        // 해당 강의의 평점 데이터
        CourseRating courseRating = course.getCourseRating_id();

        // 해당 강의평이 해당 사용자가 작성한 강의평이 아니라면, 예외 처리
        if(!Objects.equals(comment.getProfile_id().getProfile_id(), profile.getProfile_id()))
            throw new AppException(ErrorCode.NOT_COMMENT_BY_USER, comment.getProfile_id().getProfile_id());

        // 강의 엔티티의 강의평 개수
        int count = course.getCOUNT_comments();

        // 강의 평점 엔티티의 평점 평균값 수정
        courseRating.setAVG_rating((courseRating.getAVG_rating() * count + dto.getRating() - commentRating.getRating()) / count);
        courseRating.setAVG_r1_amount_of_studying((courseRating.getAVG_r1_amount_of_studying() * count + dto.getR1_amount_of_studying() - commentRating.getR1_amount_of_studying()) / count);
        courseRating.setAVG_r2_difficulty((courseRating.getAVG_r2_difficulty() * count + dto.getR2_difficulty() - commentRating.getR2_difficulty()) / count);
        courseRating.setAVG_r3_delivery_power((courseRating.getAVG_r3_delivery_power() * count + dto.getR3_delivery_power() - commentRating.getR3_delivery_power()) / count);
        courseRating.setAVG_r4_grading((courseRating.getAVG_r4_grading() * count + dto.getR4_grading() - commentRating.getR4_grading()) / count);

        // 강의 평점 엔티티의 태그 개수 수정
        if(dto.isTeach_t1_theory() && !commentRating.isTeach_t1_theory())
            courseRating.setCOUNT_teach_t1_theory(courseRating.getCOUNT_teach_t1_theory() + 1);
        else if(!dto.isTeach_t1_theory() && commentRating.isTeach_t1_theory())
            courseRating.setCOUNT_teach_t1_theory(courseRating.getCOUNT_teach_t1_theory() - 1);
        if(dto.isTeach_t2_practice() && !commentRating.isTeach_t2_practice())
            courseRating.setCOUNT_teach_t2_practice(courseRating.getCOUNT_teach_t2_practice() + 1);
        else if(!dto.isTeach_t2_practice() && commentRating.isTeach_t2_practice())
            courseRating.setCOUNT_teach_t2_practice(courseRating.getCOUNT_teach_t2_practice() - 1);
        if(dto.isTeach_t3_seminar() && !commentRating.isTeach_t3_seminar())
            courseRating.setCOUNT_teach_t3_seminar(courseRating.getCOUNT_teach_t3_seminar() + 1);
        else if(!dto.isTeach_t3_seminar() && commentRating.isTeach_t3_seminar())
            courseRating.setCOUNT_teach_t3_seminar(courseRating.getCOUNT_teach_t3_seminar() - 1);
        if(dto.isTeach_t4_discussion() && !commentRating.isTeach_t4_discussion())
            courseRating.setCOUNT_teach_t4_discussion(courseRating.getCOUNT_teach_t4_discussion() + 1);
        else if(!dto.isTeach_t4_discussion() && commentRating.isTeach_t4_discussion())
            courseRating.setCOUNT_teach_t4_discussion(courseRating.getCOUNT_teach_t4_discussion() - 1);
        if(dto.isTeach_t5_presentation() && !commentRating.isTeach_t5_presentation())
            courseRating.setCOUNT_teach_t5_presentation(courseRating.getCOUNT_teach_t5_presentation() + 1);
        else if(!dto.isTeach_t5_presentation() && commentRating.isTeach_t5_presentation())
            courseRating.setCOUNT_teach_t5_presentation(courseRating.getCOUNT_teach_t5_presentation() - 1);
        if(dto.isLearn_t1_theory() && !commentRating.isLearn_t1_theory())
            courseRating.setCOUNT_learn_t1_theory(courseRating.getCOUNT_learn_t1_theory() + 1);
        else if(!dto.isLearn_t1_theory() && commentRating.isLearn_t1_theory())
            courseRating.setCOUNT_learn_t1_theory(courseRating.getCOUNT_learn_t1_theory() - 1);
        if(dto.isLearn_t2_thesis() && !commentRating.isLearn_t2_thesis())
            courseRating.setCOUNT_learn_t2_thesis(courseRating.getCOUNT_learn_t2_thesis() + 1);
        else if(!dto.isLearn_t2_thesis() && commentRating.isLearn_t2_thesis())
            courseRating.setCOUNT_learn_t2_thesis(courseRating.getCOUNT_learn_t2_thesis() - 1);
        if(dto.isLearn_t3_exam() && !commentRating.isLearn_t3_exam())
            courseRating.setCOUNT_learn_t3_exam(courseRating.getCOUNT_learn_t3_exam() + 1);
        else if(!dto.isLearn_t3_exam() && commentRating.isLearn_t3_exam())
            courseRating.setCOUNT_learn_t3_exam(courseRating.getCOUNT_learn_t3_exam() - 1);
        if(dto.isLearn_t4_industry() && !commentRating.isLearn_t4_industry())
            courseRating.setCOUNT_learn_t4_industry(courseRating.getCOUNT_learn_t4_industry() + 1);
        else if(!dto.isLearn_t4_industry() && commentRating.isLearn_t4_industry())
            courseRating.setCOUNT_learn_t4_industry(courseRating.getCOUNT_learn_t4_industry() - 1);

        // 해당 강의평의 평점 데이터 수정
        commentRating.setRating(dto.getRating());
        commentRating.setR1_amount_of_studying(dto.getR1_amount_of_studying());
        commentRating.setR2_difficulty(dto.getR2_difficulty());
        commentRating.setR3_delivery_power(dto.getR3_delivery_power());
        commentRating.setR4_grading(dto.getR4_grading());
        commentRating.setTeach_t1_theory(dto.isTeach_t1_theory());
        commentRating.setTeach_t2_practice(dto.isTeach_t2_practice());
        commentRating.setTeach_t3_seminar(dto.isTeach_t3_seminar());
        commentRating.setTeach_t4_discussion(dto.isTeach_t4_discussion());
        commentRating.setTeach_t5_presentation(dto.isTeach_t5_presentation());
        commentRating.setLearn_t1_theory(dto.isLearn_t1_theory());
        commentRating.setLearn_t2_thesis(dto.isLearn_t2_thesis());
        commentRating.setLearn_t3_exam(dto.isLearn_t3_exam());
        commentRating.setLearn_t4_industry(dto.isLearn_t4_industry());

        // 해당 강의평 엔티티 수정
        comment.setReview(dto.getReview());
        comment.setUpdated_at(LocalDate.now());

        return course.getCourse_id();
    }
}
