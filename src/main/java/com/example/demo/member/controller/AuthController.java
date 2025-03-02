package com.example.demo.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.response.BaseResponse;
import com.example.demo.member.dto.request.LoginRequestDto;
import com.example.demo.member.dto.request.SignUpMemberRequestDto;
import com.example.demo.member.dto.response.LoginResponseDto;
import com.example.demo.member.facade.AuthFacade;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthFacade authFacade;

  @PostMapping("/signup")
  @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "회원가입 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일")
      })
  public ResponseEntity<BaseResponse<Void>> signUpMember(
      @RequestBody SignUpMemberRequestDto requestDto) {
    authFacade.signUpMember(requestDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.onSuccess(GlobalErrorCode.CREATED, null));
  }

  @PostMapping("/login")
  public ResponseEntity<BaseResponse<LoginResponseDto>> login(
      @RequestBody LoginRequestDto requestDto) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(BaseResponse.onSuccess(GlobalErrorCode.OK, authFacade.login(requestDto)));
  }
}
