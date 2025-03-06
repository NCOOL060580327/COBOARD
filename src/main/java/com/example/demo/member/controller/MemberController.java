package com.example.demo.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.global.annotation.SwaggerDocs;
import com.example.demo.global.response.BaseResponse;
import com.example.demo.member.controller.swagger.api.GetMemberByIdentifiedCodeApiDocs;
import com.example.demo.member.dto.request.GetMemberByIdentifiedCodeRequestDto;
import com.example.demo.member.dto.response.GetMemberByIdentifiedCodeResponseDto;
import com.example.demo.member.facade.MemberFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

  private final MemberFacade memberFacade;

  @GetMapping("/identified-code")
  @SwaggerDocs(GetMemberByIdentifiedCodeApiDocs.class)
  public ResponseEntity<BaseResponse<GetMemberByIdentifiedCodeResponseDto>>
      getMemberByIdentifiedCode(@RequestBody GetMemberByIdentifiedCodeRequestDto requestDto) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(BaseResponse.onSuccess(memberFacade.getMemberByIdentifiedCode(requestDto)));
  }
}
