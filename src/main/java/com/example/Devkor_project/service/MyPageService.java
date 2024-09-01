package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.CommentDto;
import com.example.Devkor_project.dto.CourseDto;
import com.example.Devkor_project.dto.ProfileDto;
import com.example.Devkor_project.entity.*;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.BookmarkRepository;
import com.example.Devkor_project.repository.CommentRatingRepository;
import com.example.Devkor_project.repository.CommentRepository;
import com.example.Devkor_project.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Service
public class MyPageService
{
    @Autowired ProfileRepository profileRepository;
    @Autowired BookmarkRepository bookmarkRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired CommentRatingRepository commentRatingRepository;
    @Autowired BCryptPasswordEncoder encoder;

    /* 마이페이지 기본 정보 서비스 */
    public ProfileDto.MyPage myPageInfo(Principal principal)
    {
        // 로그인 여부 확인 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 해당 사용자의 계정이 존재하는지 확인
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        return ProfileDto.MyPage.builder()
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
                .build();
    }

    /* 내가 작성한 강의평 정보 조회 서비스 */
    public List<CommentDto.MyPage> myComments(Principal principal, int page)
    {
        // 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 요청을 보낸 사용자의 계정이 존재하지 않으면 예외 처리
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // Pageable 객체 생성 (size = 10개)
        Pageable pageable = PageRequest.of(page, 10);

        // 사용자가 작성한 강의평 엔티티 페이지 조회
        Page<Comment> comments = commentRepository.findByProfileIdOrderNewest(profile.getProfile_id(), pageable);

        // 결과가 없으면 예외 발생
        if(comments.isEmpty())
            throw new AppException(ErrorCode.NO_RESULT, page + 1);

        // 강의평 엔티티 페이지를 강의평 dto 리스트로 변환
        List<CommentDto.MyPage> commentDtos = comments.stream()
                .map(comment -> {

                    Course course = comment.getCourse_id();                         // 강의 정보
                    CommentRating commentRating = comment.getCommentRating_id();    // 강의평 평점 정보

                    return CommentDto.entityToMyPage(comment, course, commentRating);

                })
                .toList();

        return commentDtos;
    }

    /* 내가 작성한 강의평 개수 서비스 */
    public int countMyComments(Principal principal)
    {
        // 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 요청을 보낸 사용자의 계정이 존재하지 않으면 예외 처리
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // 내가 작성한 강의평 개수 반환
        return commentRepository.countCommentByProfileId(profile.getProfile_id());
    }

    /* 내가 북마크한 강의 정보 조회 서비스 */
    public List<CourseDto.Basic> myBookmarks(Principal principal, int page)
    {
        // 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 요청을 보낸 사용자의 계정이 존재하지 않으면 예외 처리
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // Pageable 객체 생성 (size = 10개)
        Pageable pageable = PageRequest.of(page, 10);

        // 사용자가 북마크한 강의 엔티티 페이지 조회
        Page<Bookmark> bookmarks = bookmarkRepository.findByProfileId(profile.getProfile_id(), pageable);

        // 결과가 없으면 예외 발생
        if(bookmarks.isEmpty())
            throw new AppException(ErrorCode.NO_RESULT, page + 1);

        // 강의 엔티티 페이지를 강의 dto 리스트로 변환
        List<CourseDto.Basic> courseDtos = bookmarks.stream()
                .map(bookmark -> {

                    Course course = bookmark.getCourse_id();                    // 강의 정보
                    CourseRating courseRating = course.getCourseRating_id();    // 강의 평점 정보

                    // 사용자의 해당 강의 북마크 여부
                    boolean isBookmark = !bookmarkRepository.searchBookmark(profile.getProfile_id(), course.getCourse_id()).isEmpty();

                    return CourseDto.entityToBasic(course, courseRating, isBookmark);

                })
                .toList();

        return courseDtos;
    }

    /* 내가 북마크한 강의 개수 서비스 */
    public int countMyBookmarks(Principal principal)
    {
        // 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 요청을 보낸 사용자의 계정이 존재하지 않으면 예외 처리
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // 내가 북마크한 강의 개수 반환
        return bookmarkRepository.countBookmarkByProfileId(profile.getProfile_id());
    }

    /* 기본 프로필 정보 변경 서비스 */
    @Transactional
    public void updateBasic(ProfileDto.UpdateBasic dto, Principal principal)
    {
        // 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 해당 사용자의 계정이 존재하는지 확인
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // 닉네임이 1~10자리인지 체크
        if(dto.getUsername().isEmpty() || dto.getUsername().length() > 10)
            throw new AppException(ErrorCode.INVALID_USERNAME, dto.getUsername());

        // 닉네임 중복 체크
        if(!dto.getUsername().equals(profile.getUsername())) {
            profileRepository.findByUsername(dto.getUsername())
                    .ifPresent(user -> {
                        throw new AppException(ErrorCode.USERNAME_DUPLICATED, dto.getUsername());
                    });
        }

        // 학번이 7자리인지 체크
        if(dto.getStudent_id().length() != 7)
            throw new AppException(ErrorCode.INVALID_STUDENT_ID, dto.getStudent_id());

        // 학위가 'MASTER' 또는 'DEGREE'인지 체크
        if(!Objects.equals(dto.getDegree(), "MASTER") && !Objects.equals(dto.getDegree(), "DOCTOR"))
            throw new AppException(ErrorCode.INVALID_DEGREE, dto.getDegree());

        profile.setUsername(dto.getUsername());
        profile.setStudent_id(dto.getStudent_id());
        profile.setDegree(dto.getDegree());
        profile.setSemester(dto.getSemester());
        profile.setDepartment(dto.getDepartment());

        profileRepository.save(profile);
    }

    /* 비밀번호 변경 서비스 */
    @Transactional
    public void updatePassword(ProfileDto.UpdatePassword dto, Principal principal)
    {
        // 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 해당 사용자의 계정이 존재하는지 확인
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // 기존 비밀번호가 맞는지 체크
        if(!encoder.matches(dto.getOld_password(), profile.getPassword()))
            throw new AppException(ErrorCode.WRONG_PASSWORD, null);

        // 비밀번호가 8~24자리이고, 숫자와 영문을 포함하는지 체크
        boolean hasEnglish = false;
        boolean hasNumber = false;

        for (int i = 0; i < dto.getNew_password().length(); i++) {
            char ch = dto.getNew_password().charAt(i);
            if (Character.isLetter(ch)) {
                hasEnglish = true;
            } else if (Character.isDigit(ch)) {
                hasNumber = true;
            }

            if (hasEnglish && hasNumber) {
                break;
            }
        }

        if(
                dto.getNew_password().length() < 8 ||
                dto.getNew_password().length() > 24 ||
                !hasEnglish ||
                !hasNumber
        ) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, dto.getNew_password());
        }

        profile.setPassword(encoder.encode(dto.getNew_password()));
        profileRepository.save(profile);
    }
}
