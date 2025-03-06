package com.example.demo.member.service.query;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.example.demo.global.annotation.ReadOnlyTransactional;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.AuthException;
import com.example.demo.global.exception.custom.MemberException;
import com.example.demo.global.security.provider.JwtProvider;
import com.example.demo.member.entity.Member;
import com.example.demo.member.entity.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ReadOnlyTransactional
public class AuthQueryService {

  private final MemberRepository memberRepository;
  private final JwtProvider jwtProvider;

  /**
   * 주어진 이메일이 중복되지 않는지 확인합니다.
   *
   * @param email 확인할 이메일 주소
   * @throws MemberException {@code GlobalErrorCode.DUPLICATE_EMAIL} - 해당 이메일이 이미 존재하는 경우
   */
  public void isValidEmail(String email) {
    if (memberRepository.existsByEmail(email)) {
      throw new MemberException(GlobalErrorCode.DUPLICATE_EMAIL);
    }
  }

  /**
   * 주어진 refreshToken을 검증하고, 해당 회원을 조회합니다.
   *
   * @param request HTTP 요청(쿠키)
   * @return 조회된 {@link Member} 엔티티
   * @throws AuthException {@code GlobalErrorCode.INVALID_TOKEN} - 해당 토큰이 유효하지 않은 경우
   * @throws MemberException {@code GlobalErrorCode.MEMBER_NOT_FOUND} - 해당 회원이 존재하지 않은 경우
   */
  public Member getMemberByRefreshToken(HttpServletRequest request) {

    String refreshToken =
        jwtProvider
            .extractRefreshToken(request)
            .orElseThrow(() -> new AuthException(GlobalErrorCode.INVALID_TOKEN));

    Long memberId = jwtProvider.getSubject(refreshToken);

    return memberRepository
        .findById(memberId)
        .orElseThrow(() -> new MemberException(GlobalErrorCode.MEMBER_NOT_FOUND));
  }
}
