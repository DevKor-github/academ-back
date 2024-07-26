package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.CommentDto;
import com.example.Devkor_project.dto.CourseDto;
import com.example.Devkor_project.dto.ProfileDto;
import com.example.Devkor_project.entity.*;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.BookmarkRepository;
import com.example.Devkor_project.repository.CommentRepository;
import com.example.Devkor_project.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class MyPageService
{
    @Autowired ProfileRepository profileRepository;
    @Autowired BookmarkRepository bookmarkRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired BCryptPasswordEncoder encoder;

    /* 마이페이지 확인 서비스 */
    public ProfileDto.MyPage myPage(Principal principal)
    {
        // 로그인 여부 확인 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 해당 사용자의 계정이 존재하는지 확인
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // 사용자의 모든 Bookmark 엔티티 조회
        List<Bookmark> bookmarks = bookmarkRepository.findByProfileId(profile.getProfile_id());

        // Bookmark 엔티티 리스트를 CourseDto.Basic 리스트로 변환
        List<CourseDto.Basic> courseDtos = bookmarks.stream()
                .map(bookmark -> {

                    Course course = bookmark.getCourse_id();                    // 강의 정보
                    CourseRating courseRating = course.getCourseRating_id();    // 강의 평점 정보

                    // 사용자의 해당 강의 북마크 여부
                    boolean isBookmark = !bookmarkRepository.searchBookmark(profile.getProfile_id(), course.getCourse_id()).isEmpty();

                    return CourseDto.entityToBasic(course, courseRating, isBookmark);

                })
                .toList();

        // 사용자가 작성한 모든 Comment 엔티티 조회
        List<Comment> comments = commentRepository.findByProfileId(profile.getProfile_id());

        // Comment 엔티티 리스트를 CommentDto.MyPage 리스트로 변환
        List<CommentDto.MyPage> commentDtos = comments.stream()
                .map(comment -> {

                    Course course = comment.getCourse_id();                         // 강의 정보
                    CommentRating commentRating = comment.getCommentRating_id();    // 강의평 평점 정보

                    return CommentDto.entityToMyPage(comment, course, commentRating);

                })
                .toList();

        return ProfileDto.entityToMyPage(profile, courseDtos, commentDtos);
    }

    /* 비밀번호 확인 서비스 */
    public void checkPassword(ProfileDto.CheckPassword dto, Principal principal)
    {
        // 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 해당 사용자의 계정이 존재하는지 확인
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        if(!encoder.matches(dto.getPassword(), profile.getPassword()))
            throw new AppException(ErrorCode.WRONG_PASSWORD, null);
    }
}
