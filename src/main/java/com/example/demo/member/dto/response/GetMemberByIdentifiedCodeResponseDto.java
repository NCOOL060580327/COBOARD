package com.example.demo.member.dto.response;

import com.example.demo.member.entity.Member;

import lombok.Builder;

@Builder
public record GetMemberByIdentifiedCodeResponseDto(String nickname, String identifiedCode) {
  public static GetMemberByIdentifiedCodeResponseDto fromMember(Member member) {
    return GetMemberByIdentifiedCodeResponseDto.builder()
        .nickname(member.getNickname())
        .identifiedCode(member.getIdentifiedCode())
        .build();
  }
}
