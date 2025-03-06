package com.example.demo.member.facade;

import org.springframework.stereotype.Component;

import com.example.demo.member.dto.request.GetMemberByIdentifiedCodeRequestDto;
import com.example.demo.member.dto.response.GetMemberByIdentifiedCodeResponseDto;
import com.example.demo.member.service.query.MemberQueryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberFacade {

  private final MemberQueryService memberQueryService;

  public GetMemberByIdentifiedCodeResponseDto getMemberByIdentifiedCode(
      GetMemberByIdentifiedCodeRequestDto requestDto) {
    return GetMemberByIdentifiedCodeResponseDto.fromMember(
        memberQueryService.getMemberByIdentifiedCode(requestDto.identifiedCode()));
  }
}
