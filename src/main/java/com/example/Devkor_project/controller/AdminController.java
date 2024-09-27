package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.CommentDto;
import com.example.Devkor_project.dto.ResponseDto;
import com.example.Devkor_project.service.AdminService;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "ADMIN", description = "ADMIN 권한의 계정만 요청 가능한 api입니다.")
public class AdminController
{
        @Autowired AdminService adminService;
        @Autowired VersionProvider versionProvider;

        /* 대학원 강의 데이터베이스 추가 컨트톨러 */
        @PostMapping("/api/admin/insert-course-database")
        @JsonProperty("data") // data JSON 객체를 MAP<String, Object> 형식으로 매핑
        @Operation(summary = "대학원 강의 데이터베이스 추가")
        @Parameters(value = {
                @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
        })
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201", description = "아무 데이터도 반환하지 않습니다."),
                @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 401 (LOW_AUTHORITY)", description = "권한이 부족한 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 500 (UNEXPECTED_ERROR)", description = "예기치 못한 에러가 발생한 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
        })
        public ResponseEntity<ResponseDto.Success> insertCourseDatabase(@Valid @RequestBody Map<String, Object> data) {
                adminService.insertCourseDatabase(data);

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("대학원 강의 데이터베이스 추가가 정상적으로 수행되었습니다.")
                                        .data(null)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* 강의평 신고 내역 조회 컨트롤러 */
        @GetMapping("/api/admin/report-list")
        @Operation(summary = "강의평 신고 내역 조회", description = "페이지 하나 당 10개의 정보를 반환합니다.")
        @Parameters(value = {
                @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
                @Parameter(name = "page", description = "페이지 번호 ( 1부터 시작 )"),
        })
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "CommentDto.ReportList 리스트를 반환합니다.", content = @Content(schema = @Schema(implementation = CommentDto.ReportList.class))),
                @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 401 (LOW_AUTHORITY)", description = "권한이 부족한 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 404 (NO_RESULT)", description = "신고 내역이 존재하지 않는 경우 (페이지를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
        })
        public ResponseEntity<ResponseDto.Success> reportCommentList(@RequestParam("page") int page)
        {
                List<CommentDto.ReportList> data = adminService.reportCommentList(page - 1);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("강의평 신고 내역 조회가 정상적으로 수행되었습니다.")
                                        .data(data)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* 강의평 신고 내역 개수 컨트롤러 */
        @GetMapping("/api/admin/count-report")
        @Operation(summary = "강의평 신고 내역 개수")
        @Parameters(value = {
                @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
        })
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "강의평 신고 내역 개수를 반환합니다.", content = @Content(schema = @Schema(implementation = Long.class))),
        })
        public ResponseEntity<ResponseDto.Success> countNotice()
        {
                Long number = adminService.countReport();

                return ResponseEntity.status(HttpStatus.OK)
                        .body(ResponseDto.Success.builder()
                                .data(number)
                                .message("강의평 신고 내역 개수 반환에 성공하였습니다.")
                                .version(versionProvider.getVersion())
                                .build()
                        );
        }

        /* 강의평 삭제 컨트롤러 */
        @PostMapping("/api/admin/delete-comment")
        @Operation(summary = "강의평 삭제")
        @Parameters(value = {
                @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
        })
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "course_id를 반환합니다.", content = @Content(schema = @Schema(implementation = Long.class))),
                @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 400 (NOT_COMMENT_BY_USER)", description = "해당 강의평이 해당 사용자가 작성한 강의평이 아닌 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 404 (COURSE_NOT_FOUND)", description = "요청으로 보낸 강의평이 속한 강의가 존재하지 않는 경우 (course_id를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 404 (COMMENT_NOT_FOUND)", description = "요청으로 보낸 comment_id에 해당하는 강의평이 존재하지 않는 경우 (입력받은 comment_id를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "요청을 보낸 사용자의 계정이 존재하지 않는 경우 (이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
        })
        public ResponseEntity<ResponseDto.Success> deleteComment(@Valid @RequestBody CommentDto.Delete dto)
        {
                Long course_id = adminService.deleteComment(dto);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("강의평 삭제를 정상적으로 완료하였습니다.")
                                        .data(course_id)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

}
