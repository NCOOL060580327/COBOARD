package com.example.demo.member.facade;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.example.demo.member.dto.request.LoginRequestDto;
import com.example.demo.member.dto.request.SignUpMemberRequestDto;
import com.example.demo.member.dto.response.LoginWithRefreshResponseDto;
import com.example.demo.member.dto.response.RefreshResponseDto;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.command.AuthCommandService;
import com.example.demo.member.service.query.MemberQueryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthFacade {

  private final AuthCommandService authCommandService;
  private final MemberQueryService memberQueryService;

  /**
   * 새로운 회원을 등록합니다.
   *
   * @param requestDto 회원가입 요청 데이터
   */
  public void signUpMember(SignUpMemberRequestDto requestDto) {

    memberQueryService.isValidEmail(requestDto.email());

    authCommandService.signUpMember(requestDto);
  }

  public LoginWithRefreshResponseDto login(LoginRequestDto requestDto) {

    Member member = memberQueryService.getMemberByEmail(requestDto.email());

    return authCommandService.login(member, requestDto.password());
  }

  public RefreshResponseDto refresh(HttpServletRequest request) {
    return authCommandService.refresh(request);
  }
}
