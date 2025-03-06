package com.example.demo.member.facade;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.example.demo.member.dto.request.LoginRequestDto;
import com.example.demo.member.dto.request.SignUpMemberRequestDto;
import com.example.demo.member.dto.response.LoginWithRefreshResponseDto;
import com.example.demo.member.dto.response.RefreshResponseDto;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.command.AuthCommandService;
import com.example.demo.member.service.query.AuthQueryService;
import com.example.demo.member.service.query.MemberQueryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthFacade {

  private final AuthCommandService authCommandService;
  private final AuthQueryService authQueryService;
  private final MemberQueryService memberQueryService;

  /**
   * 새로운 회원을 등록합니다.
   *
   * @param requestDto 회원가입 요청 데이터
   * @return
   */
  public void signUpMember(SignUpMemberRequestDto requestDto) {

    authQueryService.isValidEmail(requestDto.email());

    authCommandService.signUpMember(requestDto);
  }

  /**
   * 이메일과 비밀번호를 통해 로그인하여 토큰과 회원 정보를 반환합니다.
   *
   * @param requestDto 로그인 요청 정보
   * @return {@link LoginWithRefreshResponseDto} 토큰과 회원정보
   */
  public LoginWithRefreshResponseDto login(LoginRequestDto requestDto) {

    Member member = memberQueryService.getMemberByEmail(requestDto.email());

    return authCommandService.login(member, requestDto.password());
  }

  /**
   * refreshToken을 검증하고 토큰을 재발급 합니다.
   *
   * @param request HTTP 요청(쿠키)
   * @return {@link RefreshResponseDto} 새로 발급받은 토큰
   */
  public RefreshResponseDto refresh(HttpServletRequest request) {

    Member member = authQueryService.getMemberByRefreshToken(request);

    return authCommandService.refresh(request, member);
  }
}
