package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.NoticeDto;
import com.example.Devkor_project.dto.ResponseDto;
import com.example.Devkor_project.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "공지사항", description = "공지사항 관련 api입니다.")
public class NoticeController
{
    @Autowired NoticeService noticeService;
    @Autowired VersionProvider versionProvider;

    /* 공지사항 목록 조회 컨트롤러 */
    @GetMapping("/api/notice/list")
    @Operation(summary = "공지사항 목록 조회", description = "항상 최신순으로 응답합니다.\n\n페이지 하나 당 10개의 공지사항을 반환합니다.")
    @Parameters(value = {
            @Parameter(name = "page", description = "페이지 번호 ( 1부터 시작 )"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "NoticeDto.List 리스트를 반환합니다.", content = @Content(schema = @Schema(implementation = NoticeDto.List.class))),
            @ApiResponse(responseCode = "실패: 404 (NO_RESULT)", description = "공지사항이 존재하지 않는 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> noticeList(@RequestParam("page") int page)
    {
        List<NoticeDto.List> data = noticeService.noticeList(page - 1);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("공지사항 목록 조회에 성공하였습니다.")
                                .data(data)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* 공지사항 상세 조회 컨트롤러 */
    @GetMapping("/api/notice/detail")
    @Operation(summary = "공지사항 상세 조회")
    @Parameters(value = {
            @Parameter(name = "notice_id", description = "상세 정보를 요청할 공지사항의 notice_id"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "NoticeDto.Detail 데이터를 반환합니다.", content = @Content(schema = @Schema(implementation = NoticeDto.Detail.class))),
            @ApiResponse(responseCode = "실패: 404 (NOTICE_NOT_FOUND)", description = "해당 notice_id 의 공지사항이 존재하지 않는 경우 (요청으로 보낸 notice_id를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
    })
    public ResponseEntity<ResponseDto.Success> noticeDetail(@RequestParam("notice_id") Long notice_id)
    {
        NoticeDto.Detail data = noticeService.noticeDetail(notice_id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDto.Success.builder()
                                .message("공지사항 상세 조회에 성공하였습니다.")
                                .data(data)
                                .version(versionProvider.getVersion())
                                .build()
                );
    }

    /* 공지사항 개수 컨트롤러 */
    @GetMapping("/api/notice/count")
    @Operation(summary = "공지사항 개수")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항 개수를 반환합니다.", content = @Content(schema = @Schema(implementation = Long.class))),
    })
    public ResponseEntity<ResponseDto.Success> countNotice()
    {
        Long number = noticeService.countNotice();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .data(number)
                        .message("공지사항 개수 반환에 성공하였습니다.")
                        .version(versionProvider.getVersion())
                        .build()
                );
    }
}
