package com.example.Devkor_project.controller;

import com.example.Devkor_project.configuration.VersionProvider;
import com.example.Devkor_project.dto.ProfileDto;
import com.example.Devkor_project.dto.ResponseDto;
import com.example.Devkor_project.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Tag(name = "로그인", description = "회원가입/로그인/로그아웃, 권한 관련 api입니다.")
public class LoginController
{
        @Autowired LoginService loginService;
        @Autowired VersionProvider versionProvider;

        /* 회원가입 컨트롤러 */
        @PostMapping("/api/signup")
        @Operation(summary = "회원가입")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201", description = "이메일을 반환합니다.", content = @Content(schema = @Schema(implementation = String.class))),
                @ApiResponse(responseCode = "실패: 400 (INVALID_STUDENT_ID)", description = "학번이 7자리가 아닌 경우 (입력받은 학번을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 400 (INVALID_PASSWORD)", description = "비밀번호가 숫자 또는 영문을 포함하지 않았거나, 8~24자리가 아닌 경우 (입력받은 비밀번호를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 400 (INVALID_USERNAME)", description = "닉네임이 1~10자리가 아닌 경우 (입력받은 닉네임을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 400 (USERNAME_DUPLICATED)", description = "닉네임이 중복된 경우 (입력받은 닉네임을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 400 (INVALID_DEGREE)", description = "학위가 ‘MASTER’ 또는 ‘DOCTOR’가 아닌 경우 (입력받은 학위를 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 400 (EMAIL_DUPLICATED)", description = "해당 이메일로 생성된 계정이 이미 존재하는 경우 (입력받은 이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 400 (WRONG_CODE)", description = "인증번호가 틀린 경우 (입력받은 이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 404 (CODE_NOT_FOUND)", description = "해당 이메일로 보낸 인증번호가 존재하지 않는 경우 (입력받은 이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
        })
        public ResponseEntity<ResponseDto.Success> signUp(@Valid @RequestBody ProfileDto.Signup dto) {
                loginService.signUp(dto);

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("회원가입을 성공하였습니다.")
                                        .data(dto.getEmail())
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* 이메일 인증번호 발송 컨트롤러 */
        @GetMapping("/api/signup/send-email")
        @Operation(summary = "이메일 인증번호 발송")
        @Parameters(value = {
                @Parameter(name = "email", description = "이메일 ( 고려대 이메일 )"),
                @Parameter(name = "purpose", description = "발송 목적 ( SIGN_UP : 회원가입 | RESET_PASSWORD : 비밀번호 초기화 )")
        })
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "이메일을 반환합니다.", content = @Content(schema = @Schema(implementation = String.class))),
                @ApiResponse(responseCode = "실패: 400 (EMAIL_NOT_KOREA)", description = "고려대 이메일 아닌 경우 (입력받은 이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 400 (INVALID_PURPOSE)", description = "purpose 인자가 SIGN_UP 또는 RESET_PASSWORD가 아닌 경우 (입력받은 목적을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 500 (UNEXPECTED_ERROR)", description = "예기치 못한 에러가 발생한 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
        })
        public ResponseEntity<ResponseDto.Success> sendAuthenticationNumber(@RequestParam("email") String email,
                                                                            @RequestParam("purpose") String purpose)
        {
                loginService.sendAuthenticationNumber(email, purpose);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("인증번호 이메일 발송에 성공하였습니다.")
                                        .data(email)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* 인증번호 확인 컨트롤러 */
        @GetMapping("/api/signup/check-email")
        @Operation(summary = "이메일 인증번호 확인")
        @Parameters(value = {
                @Parameter(name = "email", description = "이메일 ( 고려대 이메일 )"),
                @Parameter(name = "code", description = "인증번호")
        })
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "이메일을 반환합니다.", content = @Content(schema = @Schema(implementation = String.class))),
                @ApiResponse(responseCode = "실패: 400 (EMAIL_NOT_KOREA)", description = "고려대 이메일 아닌 경우 (입력받은 이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 400 (EMAIL_DUPLICATED)", description = "해당 이메일로 생성된 계정이 이미 존재하는 경우 (입력받은 이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 400 (WRONG_CODE)", description = "인증번호가 틀린 경우 (입력받은 이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 404 (CODE_NOT_FOUND)", description = "해당 이메일로 보낸 인증번호가 존재하지 않는 경우 (입력받은 이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
        })
        public ResponseEntity<ResponseDto.Success> checkAuthenticationNumber(@RequestParam("email") String email,
                                                                             @RequestParam("code") String code)
        {
                loginService.checkAuthenticationNumber(email, code);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("인증번호 이메일 확인에 성공하였습니다.")
                                        .data(email)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* 닉네임 중복 확인 컨트롤러 */
        @GetMapping("/api/signup/check-username")
        @Operation(summary = "닉네임 중복 확인")
        @Parameters(value = {
                @Parameter(name = "username", description = "닉네임")
        })
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "아무 데이터도 반환하지 않습니다."),
                @ApiResponse(responseCode = "실패: 400 (USERNAME_DUPLICATED)", description = "닉네임이 중복된 경우 (입력받은 닉네임을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
        })
        public ResponseEntity<ResponseDto.Success> checkUsername(@RequestParam("username") String username)
        {
                loginService.checkUsername(username);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("닉네임이 중복되지 않았습니다.")
                                        .data(null)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* 임시 비밀번호 발급 컨트롤러 */
        @PostMapping("/api/login/reset-password")
        @Operation(summary = "임시 비밀번호 발급")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "이메일을 반환합니다.", content = @Content(schema = @Schema(implementation = String.class))),
                @ApiResponse(responseCode = "실패: 400 (EMAIL_NOT_KOREA)", description = "고려대 이메일 아닌 경우 (입력받은 이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 400 (WRONG_CODE)", description = "인증번호가 틀린 경우 (입력받은 이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "해당 이메일로 생성된 계정이 존재하지 않는 경우 (입력받은 이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 500 (UNEXPECTED_ERROR)", description = "예기치 못한 에러가 발생한 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
        })
        public ResponseEntity<ResponseDto.Success> resetPassword(@Valid @RequestBody ProfileDto.ResetPassword dto)
        {
                loginService.resetPassword(dto);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("비밀번호가 성공적으로 초기화 되었습니다.")
                                        .data(dto.getEmail())
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* 로그인 여부 확인 컨트롤러 */
        @GetMapping("/api/check-login")
        @Operation(summary = "로그인 여부 확인")
        @Parameters(value = {
                @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {access token}")
        })
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "ProfileDto.CheckLogin 데이터를 반환합니다.", content = @Content(schema = @Schema(implementation = ProfileDto.CheckLogin.class))),
                @ApiResponse(responseCode = "실패: 401 (UNAUTHORIZED)", description = "로그인하지 않은 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "해당 이메일로 생성된 계정이 존재하지 않는 경우 (입력받은 이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
        })
        public ResponseEntity<ResponseDto.Success> checkLogin(Principal principal)
        {
                ProfileDto.CheckLogin dto = loginService.checkLogin(principal);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("로그인 여부 확인이 완료되었습니다.")
                                        .data(dto)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

        /* access token 재발급 컨트롤러 */
        @GetMapping("/api/refresh-token")
        @Operation(summary = "access token 재발급")
        @Parameters(value = {
                @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Bearer {refresh token}")
        })
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "access token을 반환합니다.", content = @Content(schema = @Schema(implementation = String.class))),
                @ApiResponse(responseCode = "실패: 401 (INVALID_TOKEN)", description = "헤더에 토큰이 없거나, Bearer로 시작하지 않거나, 해당 refresh token이 유효하지 않는 경우", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
                @ApiResponse(responseCode = "실패: 404 (EMAIL_NOT_FOUND)", description = "해당 이메일로 생성된 계정이 존재하지 않는 경우 (입력받은 이메일을 반환)", content = @Content(schema = @Schema(implementation = ResponseDto.Error.class))),
        })
        public ResponseEntity<ResponseDto.Success> refreshToken(HttpServletRequest request)
        {
                String token = loginService.refreshToken(request);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDto.Success.builder()
                                        .message("access token 재발급이 완료되었습니다.")
                                        .data(token)
                                        .version(versionProvider.getVersion())
                                        .build()
                        );
        }

}
