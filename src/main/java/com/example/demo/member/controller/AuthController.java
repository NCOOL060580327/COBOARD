package com.example.demo.member.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.response.BaseResponse;
import com.example.demo.member.dto.request.SignUpMemberRequestDto;
import com.example.demo.member.facade.AuthFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthFacade authFacade;

  @PostMapping("/signup")
  public BaseResponse<Void> signUpMember(@RequestBody SignUpMemberRequestDto requestDto) {
    authFacade.signUpMember(requestDto);
    return BaseResponse.onSuccess(GlobalErrorCode.CREATED, null);
  }
}
