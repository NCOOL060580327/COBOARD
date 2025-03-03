package com.example.demo.member.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.global.annotation.SwaggerDocs;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.response.BaseResponse;
import com.example.demo.member.controller.swagger.api.LoginApiDocs;
import com.example.demo.member.controller.swagger.api.RefreshApiDocs;
import com.example.demo.member.controller.swagger.api.SignUpApiDocs;
import com.example.demo.member.dto.request.LoginRequestDto;
import com.example.demo.member.dto.request.SignUpMemberRequestDto;
import com.example.demo.member.dto.response.LoginResponseDto;
import com.example.demo.member.dto.response.LoginWithRefreshResponseDto;
import com.example.demo.member.dto.response.RefreshResponseDto;
import com.example.demo.member.dto.response.TokenResponseDto;
import com.example.demo.member.facade.AuthFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  @Value("${jwt.refresh-token-validity}")
  private Long refreshTokenValidity;

  private final AuthFacade authFacade;

  @PostMapping("/signup")
  @SwaggerDocs(SignUpApiDocs.class)
  public ResponseEntity<BaseResponse<Void>> signUpMember(
      @RequestBody SignUpMemberRequestDto requestDto) {
    authFacade.signUpMember(requestDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.onSuccess(GlobalErrorCode.CREATED, null));
  }

  @PostMapping("/login")
  @SwaggerDocs(LoginApiDocs.class)
  public ResponseEntity<BaseResponse<LoginResponseDto>> login(
      @RequestBody LoginRequestDto requestDto) {

    LoginWithRefreshResponseDto responseDto = authFacade.login(requestDto);

    ResponseCookie refreshTokenCookie =
        ResponseCookie.from("refreshToken", responseDto.refreshToken())
            .httpOnly(true)
            .secure(false)
            .sameSite("Strict")
            .path("/api/auth/refresh")
            .maxAge(refreshTokenValidity)
            .build();

    return ResponseEntity.status(HttpStatus.OK)
        .header("Set-Cookie", refreshTokenCookie.toString())
        .body(BaseResponse.onSuccess(GlobalErrorCode.OK, LoginResponseDto.from(responseDto)));
  }

  @PostMapping("/refresh")
  @SwaggerDocs(RefreshApiDocs.class)
  public ResponseEntity<BaseResponse<TokenResponseDto>> refresh(HttpServletRequest request) {

    RefreshResponseDto responseDto = authFacade.refresh(request);

    TokenResponseDto tokenResponseDto = new TokenResponseDto(responseDto.accessToken());

    ResponseCookie refreshTokenCookie =
        ResponseCookie.from("refreshToken", responseDto.refreshToken())
            .httpOnly(true)
            .secure(false)
            .sameSite("Strict")
            .path("/api/auth/refresh")
            .maxAge(refreshTokenValidity)
            .build();

    return ResponseEntity.status(HttpStatus.OK)
        .header("Set-Cookie", refreshTokenCookie.toString())
        .body(BaseResponse.onSuccess(GlobalErrorCode.OK, tokenResponseDto));
  }
}
