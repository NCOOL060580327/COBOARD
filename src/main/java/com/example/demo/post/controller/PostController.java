package com.example.demo.post.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.demo.global.annotation.SwaggerDocs;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.response.BaseResponse;
import com.example.demo.global.security.domain.MemberDetails;
import com.example.demo.post.controller.swagger.api.CreatePostApiDocs;
import com.example.demo.post.dto.CreatePostRequestDto;
import com.example.demo.post.facade.PostFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

  private final PostFacade postFacade;

  @PostMapping("/{boardId}")
  @SwaggerDocs(CreatePostApiDocs.class)
  public ResponseEntity<BaseResponse<Void>> createPost(
      @RequestBody CreatePostRequestDto requestDto,
      @PathVariable(name = "boardId") Long boardId,
      @AuthenticationPrincipal MemberDetails memberDetails) {
    postFacade.createPost(requestDto, boardId, memberDetails.getMember());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.onSuccess(GlobalErrorCode.CREATED, null));
  }
}
