package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.*;
import com.example.Devkor_project.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "마이페이지", description = "마이페이지 관련 api입니다.")
public class MyPageController {

    @Autowired MyPageService myPageService;
    @Autowired VersionProvider versionProvider;

    /* 마이페이지 기본 정보 컨트롤러 */
    @GetMapping("/api/mypage/info")
    @Operation(summary = "마이페이지 기본 정보")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProfileDto.MyPage 데이터를 반환합니다.", content = @Content(schema = @Schema(implementation = ProfileDto.MyPage.class))),
            @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "요청을 보낸 사용자 계정이 존재하지 않는 경우 (이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> myPageInfo(Principal principal)
    {
        ProfileDto.MyPage dto = myPageService.myPageInfo(principal);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("마이페이지 기본 정보 반환이 성공적으로 수행되었습니다.")
                        .data(dto)
                        .version(versionProvider.getVersion())
                        .build()
                );
    }

    /* 강의평 열람권 구매 컨트롤러 */
    @PostMapping("/api/mypage/buy-access-authority")
    @Operation(summary = "강의평 열람권 구매")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의평 열람권 만료 날짜를 반환합니다.", content = @Content(schema = @Schema(implementation = LocalDate.class))),
            @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 400 (NOT_ENOUGH_POINT)", description = "포인트가 충분하지 않는 경우 (요청으로 보낸 item을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 400 (INVALID_ITEM)", description = "요청으로 보낸 아이템 이름이 ‘30DAYS’ 또는 ‘90DAYS’ 또는 ‘180DAYS’가 아닌 경우 (요청으로 보낸 item을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "요청을 보낸 사용자 계정이 존재하지 않는 경우 (이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> buyAccessAuthority(Principal principal,
                                                                  @Valid @RequestBody ProfileDto.BuyAccessAuth dto) {

        LocalDate expirationDate = myPageService.buyAccessAuthority(principal, dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("강의평 열람 권한 구매를 성공하였습니다.")
                                .data(expirationDate)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* 내가 작성한 강의평 정보 조회 컨트롤러 */
    @GetMapping("/api/mypage/my-comments")
    @Operation(summary = "내가 작성한 강의평 정보 조회", description = "페이지 하나 당 10개의 강의평 정보를 반환합니다.")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
            @Parameter(name = "page", description = "페이지 번호 ( 1부터 시작 )")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CommentDto.MyPage 리스트를 반환합니다.", content = @Content(schema = @Schema(implementation = CommentDto.MyPage.class))),
            @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "요청을 보낸 사용자 계정이 존재하지 않는 경우 (이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (NO_RESULT)", description = "결과가 없는 경우 (요청으로 보낸 page를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> myComments(@RequestParam("page") int page, Principal principal)
    {
        List<CommentDto.MyPage> my_comments = myPageService.myComments(principal, page - 1);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("내가 작성한 강의평 정보 조회를 정상적으로 완료하였습니다.")
                                .data(my_comments)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* 내가 작성한 강의평 개수 컨트롤러 */
    @GetMapping("/api/mypage/count-my-comments")
    @Operation(summary = "내가 작성한 강의평 개수")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의평 개수를 반환합니다.", content = @Content(schema = @Schema(implementation = int.class))),
            @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "요청을 보낸 사용자 계정이 존재하지 않는 경우 (이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> countMyComments(Principal principal)
    {
        int num = myPageService.countMyComments(principal);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("내가 작성한 강의평 개수를 정상적으로 반환하였습니다.")
                                .data(num)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* 내가 북마크한 강의 정보 조회 컨트롤러 */
    @GetMapping("/api/mypage/my-bookmarks")
    @Operation(summary = "내가 북마크한 강의 정보 조회", description = "페이지 하나 당 10개의 강의 정보를 반환합니다.")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
            @Parameter(name = "page", description = "페이지 번호 ( 1부터 시작 )")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CourseDto.Basic 리스트를 반환합니다.", content = @Content(schema = @Schema(implementation = CourseDto.Basic.class))),
            @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "요청을 보낸 사용자 계정이 존재하지 않는 경우 (이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (NO_RESULT)", description = "결과가 없는 경우 (요청으로 보낸 page를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> myBookmarks(@RequestParam("page") int page, Principal principal)
    {
        List<CourseDto.Basic> my_bookmarks = myPageService.myBookmarks(principal, page - 1);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("내가 북마크한 강의 정보 조회를 정상적으로 완료하였습니다.")
                                .data(my_bookmarks)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* 내가 북마크한 강의 개수 컨트롤러 */
    @GetMapping("/api/mypage/count-my-bookmarks")
    @Operation(summary = "내가 북마크한 강의 개수")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의 개수를 반환합니다.", content = @Content(schema = @Schema(implementation = int.class))),
            @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "요청을 보낸 사용자 계정이 존재하지 않는 경우 (이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> countMyBookmarks(Principal principal)
    {
        int num = myPageService.countMyBookmarks(principal);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("내가 북마크한 강의 개수를 정상적으로 반환하였습니다.")
                                .data(num)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* 기본 프로필 정보 변경 컨트롤러 */
    @PostMapping("/api/mypage/update-basic")
    @Operation(summary = "기본 프로필 정보 변경")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아무 데이터도 반환하지 않습니다."),
            @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 400 (INVALID_USERNAME)", description = "요청으로 보낸 닉네임이 1~10 자리를 만족하지 않을 경우 (요청으로 보낸 닉네임을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 400 (USERNAME_DUPLICATED)", description = "요청으로 보낸 닉네임이 중복된 경우 (요청으로 보낸 닉네임을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 400 (INVALID_STUDENT_ID)", description = "요청으로 보낸 학번이 7자리가 아닌 경우 (요청으로 보낸 학번을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 400 (INVALID_DEGREE)", description = "요청으로 보낸 학위가 ‘MASTER’ 또는 ‘DOCTOR’가 아닌 경우 (요청으로 보낸 학위를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "요청을 보낸 사용자 계정이 존재하지 않는 경우 (이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> updateBasic(@Valid @RequestBody ProfileDto.UpdateBasic dto,
                                                           Principal principal)
    {
        myPageService.updateBasic(dto, principal);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .data(null)
                        .message("기본 프로필 정보가 변경되었습니다.")
                        .version(versionProvider.getVersion())
                        .build()
                );
    }

    /* 비밀번호 변경 컨트롤러 */
    @PostMapping("/api/mypage/update-password")
    @Operation(summary = "비밀번호 변경")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아무 데이터도 반환하지 않습니다."),
            @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 400 (WRONG_PASSWORD)", description = "기존 비밀번호가 틀렸을 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 400 (INVALID_PASSWORD)", description = "요청으로 보낸 닉네임이 중복된 경우 (요청으로 보낸 닉네임을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "요청을 보낸 비밀번호가 숫자 또는 영문을 포함하지 않았거나, 8~24자리가 아닌 경우 (요청으로 보낸 새로운 비밀번호를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> updatePassword(@Valid @RequestBody ProfileDto.UpdatePassword dto,
                                                              Principal principal)
    {
        myPageService.updatePassword(dto, principal);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .data(null)
                        .message("비밀번호가 변경되었습니다.")
                        .version(versionProvider.getVersion())
                        .build()
                );
    }

    /* 회원 탈퇴 컨트롤러 */
    @PostMapping("/api/mypage/delete-profile")
    @Operation(summary = "회원 탈퇴")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아무 데이터도 반환하지 않습니다."),
            @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 400 (WRONG_PASSWORD)", description = "기존 비밀번호가 틀렸을 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
            @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "요청을 보낸 비밀번호가 숫자 또는 영문을 포함하지 않았거나, 8~24자리가 아닌 경우 (요청으로 보낸 새로운 비밀번호를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> deleteProfile(@Valid @RequestBody ProfileDto.Delete dto,
                                                             Principal principal)
    {
        myPageService.deleteProfile(dto, principal);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .data(null)
                        .message("회원 탈퇴가 성공적으로 수행되었습니다.")
                        .version(versionProvider.getVersion())
                        .build()
                );
    }
}
