package com.example.demo.member.dto.response;

import lombok.Builder;

@Builder
public record LoginResponseDto(
    Long memberId, String nickname, String profileImage, String accessToken) {

  public static LoginResponseDto from(LoginWithRefreshResponseDto responseDto) {
    return LoginResponseDto.builder()
        .memberId(responseDto.memberId())
        .nickname(responseDto.nickname())
        .profileImage(responseDto.profileImage())
        .accessToken(responseDto.accessToken())
        .build();
  }
}
