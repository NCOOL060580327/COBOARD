package com.example.demo.member.dto.response;

import com.example.demo.member.entity.Member;

import lombok.Builder;

@Builder
public record LoginWithRefreshResponseDto(
    Long memberId, String nickname, String profileImage, String accessToken, String refreshToken) {

  public static LoginWithRefreshResponseDto from(
      Member member, String accessToken, String refreshToken) {
    return LoginWithRefreshResponseDto.builder()
        .memberId(member.getId())
        .nickname(member.getNickname())
        .profileImage(member.getProfileImage())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }
}
