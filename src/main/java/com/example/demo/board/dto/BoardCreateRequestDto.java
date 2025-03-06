package com.example.demo.board.dto;

import java.util.List;

import com.example.demo.member.dto.response.GetMemberByIdentifiedCodeResponseDto;

public record BoardCreateRequestDto(
    String name,
    String thumbnailImage,
    List<GetMemberByIdentifiedCodeResponseDto> responseDtoList) {}
