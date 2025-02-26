package com.example.demo.member.facade;

import org.springframework.stereotype.Component;

import com.example.demo.member.dto.request.SignUpMemberRequestDto;
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
}
