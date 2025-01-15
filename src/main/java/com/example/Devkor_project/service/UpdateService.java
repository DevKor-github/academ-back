package com.example.Devkor_project.service;

import com.example.Devkor_project.dto.CommentDto;
import com.example.Devkor_project.dto.CourseDto;
import com.example.Devkor_project.entity.*;
import com.example.Devkor_project.exception.AppException;
import com.example.Devkor_project.exception.ErrorCode;
import com.example.Devkor_project.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateService
{
    private final ProfileRepository profileRepository;
    private final CourseRepository courseRepository;
    private final CourseRatingRepository courseRatingRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CommentRepository commentRepository;
    private final CommentRatingRepository commentRatingRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final TimeLocationRepository timeLocationRepository;

    /* [변경] 강의 검색 서비스 */
    public List<?> searchCourse(String keyword, String order, int page, Principal principal)
    {
        // 검색어가 2글자 미만이면 예외 발생
        if(keyword.length() < 2)
            throw new AppException(ErrorCode.SHORT_SEARCH_WORD, keyword);

        // Pageable 객체 생성 (size = 10개)
        Pageable pageable = PageRequest.of(page, 10);

        // 강의명+교수명+학수번호 검색 후, Course 엔티티 리스트 생성
        Page<Course> courses = null;

        if(order.equals("NEWEST"))
            courses = courseRepository.searchCourseByNewest(keyword, pageable);
        else if(order.equals("RATING_DESC"))
            courses = courseRepository.searchCourseByRatingDesc(keyword, pageable);
        else if(order.equals("RATING_ASC"))
            courses = courseRepository.searchCourseByRatingAsc(keyword, pageable);
        else
            throw new AppException(ErrorCode.INVALID_ORDER, order);

        // 검색 결과가 없다면 예외 발생
        if(courses.isEmpty())
            throw new AppException(ErrorCode.NO_RESULT, keyword);

        // 비로그인 시, 평점 데이터를 전달하지 않으며, 북마크 여부가 항상 false
        if(principal == null)
        {
            // Course 엔티티 리스트 -> courseDto.ExpiredBasic 리스트
            List<CourseDto.UPDATED_ExpiredBasic> courseDtos = courses.stream()
                    .map(course -> {

                        // 강의 시간 및 장소 정보
                        List<TimeLocation> timeLocations = timeLocationRepository.findByCourseId(course.getCourse_id());

                        return CourseDto.UPDATED_entityToExpiredBasic(course, timeLocations, false);
                    })
                    .toList();

            return courseDtos;
        }
        else
        {
            // 사용자 정보 확인
            String email = principal.getName();
            Profile profile = profileRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));
            Long profile_id = profile.getProfile_id();

            // 열람권 보유 시, 평점 데이터도 전달
            // 열람권 만료 시, 평점 데이터는 전달하지 않음
            if(profile.getAccess_expiration_date().isAfter(LocalDate.now()))
            {
                // Course 엔티티 리스트 -> courseDto.Basic 리스트
                List<CourseDto.UPDATED_Basic> courseDtos = courses.stream()
                        .map(course -> {

                            // 해당 강의의 평점 데이터
                            CourseRating courseRating = course.getCourseRating_id();

                            // 강의 시간 및 장소 정보
                            List<TimeLocation> timeLocations = timeLocationRepository.findByCourseId(course.getCourse_id());

                            // 사용자의 해당 강의 북마크 여부
                            boolean isBookmark = !bookmarkRepository.searchBookmark(profile_id, course.getCourse_id()).isEmpty();

                            return CourseDto.UPDATED_entityToBasic(course, courseRating, timeLocations, isBookmark);
                        })
                        .toList();

                return courseDtos;
            }
            else
            {
                // Course 엔티티 리스트 -> courseDto.ExpiredBasic 리스트
                List<CourseDto.UPDATED_ExpiredBasic> courseDtos = courses.stream()
                        .map(course -> {

                            // 강의 시간 및 장소 정보
                            List<TimeLocation> timeLocations = timeLocationRepository.findByCourseId(course.getCourse_id());

                            // 사용자의 해당 강의 북마크 여부
                            boolean isBookmark = !bookmarkRepository.searchBookmark(profile_id, course.getCourse_id()).isEmpty();

                            return CourseDto.UPDATED_entityToExpiredBasic(course, timeLocations, isBookmark);
                        })
                        .toList();

                return courseDtos;
            }
        }
    }

    /* [변경] 강의 상세 정보 서비스 */
    public Object courseDetail(Long course_id, String order, int page, Principal principal)
    {
        // 사용자가 상세 정보를 요청한 강의가 존재하지 않으면 예외 처리
        Course course = courseRepository.findById(course_id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, course_id));

        // 강의 시간 및 장소 정보
        List<TimeLocation> timeLocations = timeLocationRepository.findByCourseId(course.getCourse_id());

        // 비로그인 시, 평점 데이터와 강의평 리스트를 전달하지 않으며, 북마크 여부가 항상 false
        if(principal == null)
        {
            // CourseDto.ExpiredDetail 반환
            return CourseDto.UPDATED_entityToExpiredDetail(course, timeLocations, false);
        }
        else
        {
            // 요청을 보낸 사용자의 계정 정보
            String principalEmail = principal.getName();
            Profile principalProfile = profileRepository.findByEmail(principalEmail)
                    .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, principalEmail));
            Long principalProfile_id = principalProfile.getProfile_id();

            // 열람권 보유 시, 평점 데이터와 강의평 리스트도 전달
            // 열람권 만료 시, 평점 데이터와 강의평 리스트를 전달하지 않음
            if(principalProfile.getAccess_expiration_date().isAfter(LocalDate.now()))
            {
                // 해당 강의의 평점 데이터가 존재하지 않으면 예외 처리
                CourseRating courseRating = courseRatingRepository.findById(course.getCourseRating_id().getCourseRating_id())
                        .orElseThrow(() -> new AppException(ErrorCode.COURSE_RATING_NOT_FOUND, course.getCourseRating_id().getCourseRating_id()));

                // Pageable 객체 생성 (size = 10개)
                Pageable pageable = PageRequest.of(page, 10);

                // 해당 강의의 강의평들 검색
                Page<Comment> comments = null;

                if(order.equals("NEWEST"))
                    comments = commentRepository.findByCourseIdOrderNewest(course_id, pageable);
                else if(order.equals("RATING_DESC"))
                    comments = commentRepository.findByCourseIdOrderRatingDesc(course_id, pageable);
                else if(order.equals("RATING_ASC"))
                    comments = commentRepository.findByCourseIdOrderRatingAsc(course_id, pageable);
                else if(order.equals("LIKES_DESC"))
                    comments = commentRepository.findByCourseIdOrderLikesDesc(course_id, pageable);
                else if(order.equals("LIKES_ASC"))
                    comments = commentRepository.findByCourseIdOrderLikesAsc(course_id, pageable);
                else
                    throw new AppException(ErrorCode.INVALID_ORDER, order);

                // 강의평 엔티티 리스트를 강의평 dto 리스트로 변환
                List<CommentDto.Detail> commentDtos = comments.stream()
                        .map(comment -> {

                            // 해당 강의평을 작성한 사용자의 profile_id가 존재하지 않으면 예외 처리
                            Profile profile = profileRepository.findById(comment.getProfile_id().getProfile_id())
                                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, comment.getProfile_id().getProfile_id()));

                            // 해당 강의평의 평점 데이터가 존재하지 않으면 예외 처리
                            CommentRating commentRating = commentRatingRepository.findById(comment.getCommentRating_id().getCommentRating_id())
                                    .orElseThrow(() -> new AppException(ErrorCode.COMMENT_RATING_NOT_FOUND, comment.getCommentRating_id().getCommentRating_id()));

                            CommentLike commentLike = commentLikeRepository.searchCommentLike(principalProfile_id, comment.getComment_id());

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
                                    .already_like(commentLike != null)
                                    .build();

                        })
                        .toList();

                // 사용자의 해당 강의 북마크 여부
                boolean isBookmark = !bookmarkRepository.searchBookmark(principalProfile_id, course.getCourse_id()).isEmpty();

                // course 엔티티와 강의평 dto 리스트로 CourseDto.Detail 만들어서 반환
                return CourseDto.UPDATED_entityToDetail(course, courseRating, commentDtos, timeLocations, isBookmark);
            }
            else
            {
                // 사용자의 해당 강의 북마크 여부
                boolean isBookmark = !bookmarkRepository.searchBookmark(principalProfile_id, course.getCourse_id()).isEmpty();

                // CourseDto.ExpiredDetail 반환
                return CourseDto.UPDATED_entityToExpiredDetail(course, timeLocations, isBookmark);
            }
        }
    }

    /* [변경] 강의평 작성 시작 서비스 */
    public CourseDto.UPDATED_Basic startInsertComment(Principal principal, Long course_id)
    {
        // 강의평 작성 시작 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 강의평 작성 시작 요청을 보낸 사용자의 계정이 존재하지 않으면, 예외 처리
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // 사용자가 강의평을 추가할 강의가 존재하지 않으면, 예외 처리
        Course course = courseRepository.findById(course_id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, course_id));

        // 강의 시간 및 장소 정보
        List<TimeLocation> timeLocations = timeLocationRepository.findByCourseId(course.getCourse_id());

        // 해당 사용자가 이미 해당 강의에 강의평을 달았다면, 예외 처리
        List<Comment> comment = commentRepository.searchComment(profile.getProfile_id(), course_id);
        if(!comment.isEmpty())
            throw new AppException(ErrorCode.ALREADY_EXIST, course_id);

        // 해당 강의의 평점 데이터
        CourseRating courseRating = course.getCourseRating_id();

        // 사용자의 해당 강의 북마크 여부
        boolean isBookmark = !bookmarkRepository.searchBookmark(profile.getProfile_id(), course.getCourse_id()).isEmpty();

        return CourseDto.UPDATED_entityToBasic(course, courseRating, timeLocations, isBookmark);
    }

    /* [변경] 강의평 수정 시작 서비스 */
    public CommentDto.UPDATED_StartUpdate startUpdateComment(Principal principal, Long comment_id)
    {
        // 강의평 작성 시작 요청을 보낸 사용자의 계정 이메일
        String email = principal.getName();

        // 강의평 작성 시작 요청을 보낸 사용자의 계정이 존재하지 않으면, 예외 처리
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email));

        // 사용자가 수정할 강의평이 존재하지 않으면, 예외 처리
        Comment comment = commentRepository.findById(comment_id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND, comment_id));

        // 강의 정보
        Course course = comment.getCourse_id();

        // 강의 평점 정보
        CourseRating courseRating = course.getCourseRating_id();

        // 강의 시간 및 장소 정보
        List<TimeLocation> timeLocations = timeLocationRepository.findByCourseId(course.getCourse_id());

        // 해당 강의평이 해당 사용자가 작성한 강의평이 아니라면, 예외 처리
        if(!Objects.equals(comment.getProfile_id().getProfile_id(), profile.getProfile_id()))
            throw new AppException(ErrorCode.NOT_COMMENT_BY_USER, null);

        CommentRating commentRating = comment.getCommentRating_id();

        return CommentDto.UPDATED_entityToStartUpdate(course, courseRating, comment, commentRating, timeLocations);
    }

    /* [변경] 내가 작성한 강의평 정보 조회 서비스 */
    public List<CommentDto.UPDATED_MyPage> myComments(Principal principal, int page)
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
        List<CommentDto.UPDATED_MyPage> commentDtos = comments.stream()
                .map(comment -> {

                    Course course = comment.getCourse_id();                         // 강의 정보
                    CommentRating commentRating = comment.getCommentRating_id();    // 강의평 평점 정보
                    List<TimeLocation> timeLocations = timeLocationRepository.findByCourseId(course.getCourse_id()); // 강의 시간 및 장소 정보

                    return CommentDto.UPDATED_entityToMyPage(comment, course, commentRating, timeLocations);

                })
                .toList();

        return commentDtos;
    }

    /* [변경] 내가 북마크한 강의 정보 조회 서비스 */
    public List<CourseDto.UPDATED_Basic> myBookmarks(Principal principal, int page)
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
        List<CourseDto.UPDATED_Basic> courseDtos = bookmarks.stream()
                .map(bookmark -> {

                    Course course = bookmark.getCourse_id();                    // 강의 정보
                    CourseRating courseRating = course.getCourseRating_id();    // 강의 평점 정보
                    List<TimeLocation> timeLocations = timeLocationRepository.findByCourseId(course.getCourse_id()); // 강의 시간 및 장소 정보

                    // 사용자의 해당 강의 북마크 여부
                    boolean isBookmark = !bookmarkRepository.searchBookmark(profile.getProfile_id(), course.getCourse_id()).isEmpty();

                    return CourseDto.UPDATED_entityToBasic(course, courseRating, timeLocations, isBookmark);

                })
                .toList();

        return courseDtos;
    }
}
