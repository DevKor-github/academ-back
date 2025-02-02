package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.CommentDto;
import com.example.Devkor_project.dto.CourseDto;
import com.example.Devkor_project.dto.ResponseDto;
import com.example.Devkor_project.service.CourseService;
import com.example.Devkor_project.service.UpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "업데이트", description = "변경되었지만 프론트 측에서 아직 조치를 취하지 않은 api입니다.")
public class UpdateController
{
    private final UpdateService updateService;
    private final VersionProvider versionProvider;

    /* [변경] 강의 검색 컨트롤러 */
    @GetMapping("/api/update/course/search")
    @Operation(summary = "[변경] 강의 검색", description = "**avg_rating, avg_r1_amount_of_studying, avg_r2_difficulty, avg_r3_delivery_power, avg_r4_grading**의 경우, 등록된 강의평이 존재하지 않는다면 0.0 값을 가집니다.\n\n**credit**의 경우, 값이 null일 수 있습니다.\n\n**time_locations**의 경우, 비어있거나 값이 여러 개일 수도 있습니다.\n\n**semester**의 경우 다음과 같은 값을 가집니다.\n- 1R : 1학기\n- 1S : 여름계절\n- 2R : 2학기\n- 2W : 겨울계절\n- M0 : Module0\n- M1 : Module1\n- M2 : Module2\n- M3 : Module3\n- M4 : Module4\n- M5 : Module5\n- M6 : Module6\n- M7 : Module7\n\n페이지 하나 당 10개의 결과를 반환합니다.")
    @Parameters(value = {
            @Parameter(name = "keyword", description = "검색어"),
            @Parameter(name = "order", description = "검색 결과 배치 순서 ( NEWEST : 최신순 | RATING_DESC : 평점 높은순 | RATING_ASC : 평점 낮은순 )"),
            @Parameter(name = "page", description = "페이지 번호 ( 1부터 시작 )")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200 (열람권 보유)", description = "CourseDto.Basic 리스트를 반환합니다.", content = @Content(schema = @Schema(implementation = CourseDto.UPDATED_Basic.class))),
            @ApiResponse(responseCode = "200 (열람권 만료, 비로그인)", description = "CourseDto.ExpiredBasic 리스트를 반환합니다.", content = @Content(schema = @Schema(implementation = CourseDto.UPDATED_ExpiredBasic.class))),
            @ApiResponse(responseCode = "실패: 400 (SHORT_SEARCH_WORD)", description = "검색어가 1글자 이하인 경우 (입력받은 검색어를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 400 (INVALID_ORDER)", description = "입력받은 order 인자가 올바르지 않은 경우 (입력받은 배치 순서를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (NO_RESULT)", description = "검색 결과가 존재하지 않는 경우 (입력받은 검색어를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "요청을 보낸 사용자의 계정이 존재하지 않는 경우 (이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> searchCourse(@RequestParam("keyword") String keyword,
                                                            @RequestParam("order") String order,
                                                            @RequestParam("page") int page,
                                                            Principal principal)
    {
        List<?> courses = updateService.searchCourse(keyword, order, page - 1, principal);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("강의 검색이 성공적으로 수행되었습니다.")
                        .data(courses)
                        .version(versionProvider.getVersion())
                        .build()
                );
    }

    /* [변경] 강의 상세 정보 컨트롤러 */
    @GetMapping("api/update/course/detail")
    @Operation(summary = "[변경] 강의 상세 정보", description = "**avg_rating, avg_r1_amount_of_studying, avg_r2_difficulty, avg_r3_delivery_power, avg_r4_grading**의 경우, 등록된 강의평이 존재하지 않는다면 0.0 값을 가집니다.\n\n**credit**의 경우, 값이 null일 수 있습니다.\n\n**time_locations**의 경우, 비어있거나 값이 여러 개일 수도 있습니다.\n\n**semester**의 경우 다음과 같은 값을 가집니다.\n- 1R : 1학기\n- 1S : 여름계절\n- 2R : 2학기\n- 2W : 겨울계절\n- M0 : Module0\n- M1 : Module1\n- M2 : Module2\n- M3 : Module3\n- M4 : Module4\n- M5 : Module5\n- M6 : Module6\n- M7 : Module7\n\n페이지 하나 당 10개의 강의평 정보를 반환합니다.")
    @Parameters(value = {
            @Parameter(name = "course_id", description = "상세 정보를 요청할 강의의 course_id"),
            @Parameter(name = "order", description = "강의평 배치 순서 ( NEWEST : 최신순 | RATING_DESC : 평점 높은순 | RATING_ASC : 평점 낮은순 | LIKES_DESC : 좋아요 많은순 | LIKES_ASC : 좋아요 적은순 )"),
            @Parameter(name = "page", description = "페이지 번호 ( 1부터 시작 )"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200 (열람권 보유)", description = "CourseDto.Detail 데이터를 반환합니다.", content = @Content(schema = @Schema(implementation = CourseDto.UPDATED_Detail.class))),
            @ApiResponse(responseCode = "실패: 401 (NO_ACCESS_AUTHORITY)", description = "강의평 열람권이 만료된 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (COURSE_NOT_FOUND)", description = "상세 정보를 요청한 course_id에 대한 강의가 존재하지 않는 경우 (입력받은 course_id를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (USER_NOT_FOUND)", description = "특정 강의평에 대한 사용자 데이터가 존재하지 않는 경우 (profile_id를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (COURSE_RATING_NOT_FOUND)", description = "특정 강의에 대한 평점 데이터가 존재하지 않는 경우 (courseRating_id를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (COMMENT_RATING_NOT_FOUND)", description = "특정 강의평에 대한 평점 데이터가 존재하지 않는 경우 (commentRating_id를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> courseDetail(@RequestParam("course_id") Long course_id,
                                                            @RequestParam("order") String order,
                                                            @RequestParam("page") int page,
                                                            Principal principal)
    {
        Object dto = updateService.courseDetail(course_id, order, page - 1, principal);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("강의 상세 정보가 성공적으로 반환되었습니다.")
                                .data(dto)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* [변경] 강의평 작성 시작 컨트롤러 */
    @GetMapping("/api/update/course/start-insert-comment")
    @Operation(summary = "[변경] 강의평 작성 시작")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
            @Parameter(name = "course_id", description = "강의평을 추가할 강의의 course_id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CourseDto.Basic 데이터를 반환합니다.", content = @Content(schema = @Schema(implementation = CourseDto.UPDATED_Basic.class))),
            @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 400 (ALREADY_EXIST)", description = "해당 사용자가 해당 강의에 이미 강의평을 등록한 경우 (입력받은 course_id를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (COURSE_NOT_FOUND)", description = "요청으로 보낸 course_id에 해당하는 강의가 존재하지 않는 경우 (입력받은 course_id를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "요청을 보낸 사용자의 계정이 존재하지 않는 경우 (이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> startInsertComment(Principal principal,
                                                                  @RequestParam("course_id") Long course_id)
    {
        CourseDto.UPDATED_Basic dto = updateService.startInsertComment(principal, course_id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("강의평 작성을 시작합니다.")
                                .data(dto)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* [변경] 강의평 수정 시작 컨트롤러 */
    @GetMapping("/api/update/course/start-update-comment")
    @Operation(summary = "[변경] 강의평 수정 시작")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
            @Parameter(name = "comment_id", description = "수정할 강의평의 comment_id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CommentDto.StartUpdate 데이터를 반환합니다.", content = @Content(schema = @Schema(implementation = CommentDto.UPDATED_StartUpdate.class))),
            @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 400 (NOT_COMMENT_BY_USER)", description = "해당 강의평이 해당 사용자가 작성한 강의평이 아닌 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (COMMENT_NOT_FOUND)", description = "요청으로 보낸 comment_id에 해당하는 강의평이 존재하지 않는 경우 (입력받은 comment_id를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "요청을 보낸 사용자의 계정이 존재하지 않는 경우 (이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> startUpdateComment(Principal principal,
                                                                  @RequestParam("comment_id") Long comment_id)
    {
        CommentDto.UPDATED_StartUpdate dto = updateService.startUpdateComment(principal, comment_id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("강의평 수정을 시작합니다.")
                                .data(dto)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* [변경] 내가 작성한 강의평 정보 조회 컨트롤러 */
    @GetMapping("/api/update/mypage/my-comments")
    @Operation(summary = "[변경] 내가 작성한 강의평 정보 조회", description = "페이지 하나 당 10개의 강의평 정보를 반환합니다.")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
            @Parameter(name = "page", description = "페이지 번호 ( 1부터 시작 )")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CommentDto.MyPage 리스트를 반환합니다.", content = @Content(schema = @Schema(implementation = CommentDto.UPDATED_MyPage.class))),
            @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "요청을 보낸 사용자 계정이 존재하지 않는 경우 (이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (NO_RESULT)", description = "결과가 없는 경우 (요청으로 보낸 page를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> myComments(@RequestParam("page") int page, Principal principal)
    {
        List<CommentDto.UPDATED_MyPage> my_comments = updateService.myComments(principal, page - 1);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("내가 작성한 강의평 정보 조회를 정상적으로 완료하였습니다.")
                                .data(my_comments)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* [변경] 내가 북마크한 강의 정보 조회 컨트롤러 */
    @GetMapping("/api/update/mypage/my-bookmarks")
    @Operation(summary = "[변경] 내가 북마크한 강의 정보 조회", description = "페이지 하나 당 10개의 강의 정보를 반환합니다.")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
            @Parameter(name = "page", description = "페이지 번호 ( 1부터 시작 )")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CourseDto.Basic 리스트를 반환합니다.", content = @Content(schema = @Schema(implementation = CourseDto.UPDATED_Basic.class))),
            @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "요청을 보낸 사용자 계정이 존재하지 않는 경우 (이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (NO_RESULT)", description = "결과가 없는 경우 (요청으로 보낸 page를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> myBookmarks(@RequestParam("page") int page, Principal principal)
    {
        List<CourseDto.UPDATED_Basic> my_bookmarks = updateService.myBookmarks(principal, page - 1);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("내가 북마크한 강의 정보 조회를 정상적으로 완료하였습니다.")
                                .data(my_bookmarks)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }
}
