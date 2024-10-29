package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.*;
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

        /* 강의 정보 동기화 컨트톨러 */
        @PostMapping("/api/admin/check-course-synchronization")
        @Operation(summary = "강의 정보 동기화")
        @Parameters(value = {
                @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
        })
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "CourseDto.CheckSynchronization 리스트를 반환합니다.", content = @Content(schema = @Schema(implementation = CourseDto.CheckSynchronization.class))),
        })
        public ResponseEntity<ResponseDto.Success> checkCourseSynchronization(@Valid @RequestBody CrawlingDto.Synchronization dto)
        {
                CourseDto.CheckSynchronization data = adminService.checkCourseSynchronization(dto);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("강의 정보 동기화를 성공적으로 수행하였습니다.")
                                        .data(data)
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

        /* 월 단위 경로별 트래픽 확인 컨트롤러 */
        @GetMapping("/api/admin/traffic-monthly")
        @Operation(summary = "월 단위 경로별 트래픽 확인")
        @Parameters(value = {
                @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
                @Parameter(name = "year", description = "연도"),
                @Parameter(name = "month", description = "월"),
        })
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "TrafficDto.Month 리스트를 반환합니다.", content = @Content(schema = @Schema(implementation = TrafficDto.Month.class))),
                @ApiResponse(responseCode = "실패: 404 (TRAFFIC_NOT_FOUND)", description = "해당 기간 동안 요청이 들어오지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
        })
        public ResponseEntity<ResponseDto.Success> trafficMonthly(@RequestParam("year") String year,
                                                                  @RequestParam("month") Byte month)
        {
                List<TrafficDto.Month> data = adminService.trafficMonthly(year, month);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("월 단위 경로별 트래픽 확인을 정상적으로 완료하였습니다.")
                                        .data(data)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* 연도 단위 월별 트래픽 확인 컨트롤러 */
        @GetMapping("/api/admin/traffic-yearly")
        @Operation(summary = "연도 단위 월별 트래픽 확인")
        @Parameters(value = {
                @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}"),
                @Parameter(name = "year", description = "연도"),
        })
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "TrafficDto.Year 리스트를 반환합니다.", content = @Content(schema = @Schema(implementation = TrafficDto.Year.class))),
        })
        public ResponseEntity<ResponseDto.Success> trafficYearly(@RequestParam("year") String year)
        {
                List<TrafficDto.Year> data = adminService.trafficYearly(year);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("연도 단위 월별 트래픽 확인을 정상적으로 완료하였습니다.")
                                        .data(data)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

}
