package com.example.demo.post.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.demo.global.annotation.SwaggerDocs;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.response.BaseResponse;
import com.example.demo.global.security.domain.MemberDetails;
import com.example.demo.post.controller.swagger.api.CreatePostApiDocs;
import com.example.demo.post.controller.swagger.api.GetPostListInBoardApiDocs;
import com.example.demo.post.dto.CreatePostRequestDto;
import com.example.demo.post.dto.GetPostListInBoardResponseDto;
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

  @GetMapping("/{boardId}")
  @SwaggerDocs(GetPostListInBoardApiDocs.class)
  public ResponseEntity<BaseResponse<Page<GetPostListInBoardResponseDto>>> getPostListInBoard(
      @PathVariable(name = "boardId") Long boardId,
      @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            BaseResponse.onSuccess(
                GlobalErrorCode.OK, postFacade.getPostListInBoard(boardId, pageable)));
  }
}
