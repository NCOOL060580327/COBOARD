package com.example.demo.Member;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.AuthException;
import com.example.demo.global.exception.custom.MemberException;
import com.example.demo.global.security.provider.JwtProvider;
import com.example.demo.member.entity.Member;
import com.example.demo.member.entity.MemberRole;
import com.example.demo.member.entity.Password;
import com.example.demo.member.entity.Tier;
import com.example.demo.member.entity.repository.MemberRepository;
import com.example.demo.member.service.query.AuthQueryService;

@ExtendWith(MockitoExtension.class)
public class AuthQueryServiceTest {

  @Mock private MemberRepository memberRepository;
  @Mock private JwtProvider jwtProvider;

  @InjectMocks private AuthQueryService authQueryService;

  private final Long testId = Long.parseLong(MemberTestConst.TEST_ID.getValue());
  private final String testEmail = MemberTestConst.TEST_EMAIL.getValue();
  private final String testPassword = MemberTestConst.TEST_PASSWORD.getValue();
  private final String testNickname = MemberTestConst.TEST_NICKNAME.getValue();
  private final String testProfileImage = MemberTestConst.TEST_PROFILE_IMAGE.getValue();
  private final String testAccessToken = MemberTestConst.TEST_ACCESS_TOKEN.getValue();
  private final String testRefreshToken = MemberTestConst.TEST_REFRESH_TOKEN.getValue();

  @BeforeEach
  void setUp() {}

  @Nested
  @DisplayName("토큰을 통해 유저를 조회 때")
  class getMemberByRefreshToken {

    Member testMember =
        Member.builder()
            .id(testId)
            .email(testEmail)
            .password(new Password(testPassword))
            .nickname(testNickname)
            .profileImage(testProfileImage)
            .memberRole(MemberRole.USER)
            .tier(Tier.UNRANK)
            .build();

    HttpServletRequest request = mock(HttpServletRequest.class);

    @Test
    @DisplayName("사용자를 반환합니다.")
    void getMemberByRefreshToken_Success() {
      // give
      when(jwtProvider.extractRefreshToken(request)).thenReturn(Optional.of(testRefreshToken));
      when(jwtProvider.getSubject(testRefreshToken)).thenReturn(testId);
      when(memberRepository.findById(testId)).thenReturn(Optional.of(testMember));

      // when
      Member member = authQueryService.getMemberByRefreshToken(request);

      // then
      assertNotNull(member, "반환된 사용자가 null이면 안됩니다.");
      assertEquals(testMember, member, "반환된 값이 사용자와 일치해야 합니다.");
      verify(jwtProvider, times(1)).extractRefreshToken(request);
      verify(jwtProvider, times(1)).getSubject(testRefreshToken);
      verify(memberRepository, times(1)).findById(testId);
    }

    @Test
    @DisplayName("토큰이 유효하지 않으면 예외를 발생시킵니다.")
    void getMemberByRefreshToken_Fail_Invalid_Token() {
      // give
      when(jwtProvider.extractRefreshToken(request)).thenReturn(Optional.empty());

      // when
      AuthException exception =
          assertThrows(
              AuthException.class, () -> authQueryService.getMemberByRefreshToken(request));

      // then
      assertEquals(
          GlobalErrorCode.INVALID_TOKEN, exception.getErrorCode(), "에러 코드는 INVALID_TOKEN이어야 합니다.");
      verify(jwtProvider, times(1)).extractRefreshToken(request);
      verify(jwtProvider, never()).getSubject(anyString());
      verify(memberRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("사용자가 존재하지 않으면 예외를 발생시킵니다.")
    void getMemberByRefreshToken_Fail_Member_Not_Found() {
      // give
      when(jwtProvider.extractRefreshToken(request)).thenReturn(Optional.of(testRefreshToken));
      when(jwtProvider.getSubject(testRefreshToken)).thenReturn(testId);
      when(memberRepository.findById(testId)).thenReturn(Optional.empty());

      // when
      MemberException exception =
          assertThrows(
              MemberException.class, () -> authQueryService.getMemberByRefreshToken(request));

      // then
      assertEquals(
          GlobalErrorCode.MEMBER_NOT_FOUND,
          exception.getErrorCode(),
          "에러 코드는 MEMBER_NOT_FOUND이어야 합니다.");
      verify(jwtProvider, times(1)).extractRefreshToken(request);
      verify(jwtProvider, times(1)).getSubject(testRefreshToken);
      verify(memberRepository, times(1)).findById(testId);
    }
  }
}
