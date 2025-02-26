package com.example.demo.member.dto.response;

import com.example.demo.member.entity.Member;

import lombok.Builder;

@Builder
public record LoginResponseDto(
    Long memberId, String nickname, String profileImage, String accessToken, String refreshToken) {

  public static LoginResponseDto from(Member member, String accessToken, String refreshToken) {
    return LoginResponseDto.builder()
        .memberId(member.getId())
        .nickname(member.getNickname())
        .profileImage(member.getProfileImage())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }
}
