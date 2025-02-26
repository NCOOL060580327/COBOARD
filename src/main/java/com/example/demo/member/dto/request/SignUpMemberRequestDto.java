package com.example.demo.member.dto.request;

public record SignUpMemberRequestDto(
    String email, String password, String nickName, String profileImage) {}
