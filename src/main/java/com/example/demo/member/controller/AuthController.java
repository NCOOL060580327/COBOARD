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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthFacade authFacade;

  @PostMapping("/signup")
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
