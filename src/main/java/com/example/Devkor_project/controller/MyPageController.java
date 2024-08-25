package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.CommentDto;
import com.example.Devkor_project.dto.CourseDto;
import com.example.Devkor_project.dto.ProfileDto;
import com.example.Devkor_project.dto.ResponseDto;
import com.example.Devkor_project.service.MyPageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class MyPageController {

    @Autowired MyPageService myPageService;
    @Autowired VersionProvider versionProvider;

    /* 마이페이지 기본 정보 컨트롤러 */
    @GetMapping("/api/mypage/info")
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

    /* 내가 작성한 강의평 정보 조회 컨트롤러 */
    @GetMapping("/api/mypage/my-comments")
    public ResponseEntity<ResponseDto.Success> myComments(Principal principal, int page)
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

    /* 내가 북마크한 강의 정보 조회 컨트롤러 */
    @GetMapping("/api/mypage/my-bookmarks")
    public ResponseEntity<ResponseDto.Success> myBookmarks(Principal principal, int page)
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

    /* 비밀번호 확인 컨트롤러 */
    @PostMapping("/api/mypage/check-password")
    public ResponseEntity<ResponseDto.Success> checkPassword(@Valid @RequestBody ProfileDto.CheckPassword dto,
                                                             Principal principal)
    {
        myPageService.checkPassword(dto, principal);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.Success.builder()
                        .message("비밀번호가 확인되었습니다.")
                        .data(null)
                        .version(versionProvider.getVersion())
                        .build()
                );
    }

    /* 기본 프로필 정보 변경 컨트롤러 */
    @PostMapping("/api/mypage/update-basic")
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
}
