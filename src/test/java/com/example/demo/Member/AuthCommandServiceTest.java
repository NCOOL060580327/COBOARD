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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.AuthException;
import com.example.demo.global.security.provider.JwtProvider;
import com.example.demo.member.dto.request.SignUpMemberRequestDto;
import com.example.demo.member.dto.response.LoginWithRefreshResponseDto;
import com.example.demo.member.dto.response.RefreshResponseDto;
import com.example.demo.member.entity.Member;
import com.example.demo.member.entity.MemberRole;
import com.example.demo.member.entity.Password;
import com.example.demo.member.entity.Tier;
import com.example.demo.member.entity.repository.MemberRepository;
import com.example.demo.member.service.command.AuthCommandService;

@ExtendWith(MockitoExtension.class)
public class AuthCommandServiceTest {

  @Mock private MemberRepository memberRepository;

  @Mock private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Mock private JwtProvider jwtProvider;

  @InjectMocks private AuthCommandService authCommandService;

  private SignUpMemberRequestDto requestDto;

  private final Long testId = Long.parseLong(MemberTestConst.TEST_ID.getValue());
  private final String testEmail = MemberTestConst.TEST_EMAIL.getValue();
  private final String testPassword = MemberTestConst.TEST_PASSWORD.getValue();
  private final String testNickname = MemberTestConst.TEST_NICKNAME.getValue();
  private final String testProfileImage = MemberTestConst.TEST_PROFILE_IMAGE.getValue();
  private final String testAccessToken = MemberTestConst.TEST_ACCESS_TOKEN.getValue();
  private final String testRefreshToken = MemberTestConst.TEST_REFRESH_TOKEN.getValue();

  @BeforeEach
  void setUp() {
    requestDto =
        new SignUpMemberRequestDto(testEmail, testPassword, testNickname, testProfileImage);
  }

  @Nested
  @DisplayName("Member 객체 생성 시")
  class CreateMember {
    @Test
    @DisplayName("Member 객체를 생성합니다.")
    void createMember_Success() {
      // Given
      MemberRole memberRole = MemberRole.USER;
      String encryptedPassword = "encryptedPassword";
      when(bCryptPasswordEncoder.encode(requestDto.password())).thenReturn(encryptedPassword);

      // When
      Member member = authCommandService.createMember(requestDto, memberRole);

      // Then
      assertNotNull(member, "사용자가 null이면 안 됩니다");

      assertAll(
          "사용자 속성 검증",
          () -> assertEquals(requestDto.email(), member.getEmail(), "이메일이 요청과 일치해야 합니다"),
          () ->
              assertEquals(
                  encryptedPassword, member.getPassword().getPassword(), "비밀번호가 암호화되어야 합니다"),
          () -> assertEquals(requestDto.nickName(), member.getNickname(), "닉네임이 요청과 일치해야 합니다"),
          () ->
              assertTrue(
                  member.getIdentifiedCode().startsWith(requestDto.nickName() + "#"),
                  "식별자는 닉네임#으로 시작해야 합니다"),
          () ->
              assertEquals(
                  requestDto.profileImage(), member.getProfileImage(), "프로필 이미지가 요청과 일치해야 합니다"),
          () -> assertEquals(memberRole, member.getMemberRole(), "회원 역할이 입력과 일치해야 합니다"),
          () -> assertEquals(Tier.UNRANK, member.getTier(), "티어는 UNRANK여야 합니다"));

      verify(bCryptPasswordEncoder, times(1)).encode(requestDto.password());
    }
  }

  @Nested
  @DisplayName("회원가입 시")
  class SignUpMember {
    @Test
    @DisplayName("사용자를 등록합니다.")
    void signUpMember_Success() {
      // give
      MemberRole memberRole = MemberRole.USER;
      String encryptedPassword = "encryptedPassword";

      when(bCryptPasswordEncoder.encode(requestDto.password())).thenReturn(encryptedPassword);

      // when
      authCommandService.signUpMember(requestDto);

      // Then
      verify(memberRepository, times(1))
          .save(
              argThat(
                  member -> {
                    assertAll(
                        "회원 속성 검증",
                        () -> assertNotNull(member, "회원이 null이면 안 됩니다"),
                        () ->
                            assertEquals(
                                requestDto.email(), member.getEmail(), "이메일이 요청과 일치해야 합니다"),
                        () ->
                            assertEquals(
                                encryptedPassword,
                                member.getPassword().getPassword(),
                                "비밀번호가 암호화되어야 합니다"), // String 비교
                        () ->
                            assertEquals(
                                requestDto.nickName(), member.getNickname(), "닉네임이 요청과 일치해야 합니다"),
                        () ->
                            assertTrue(
                                member.getIdentifiedCode().startsWith(requestDto.nickName() + "#"),
                                "식별자는 닉네임#으로 시작해야 합니다"),
                        () ->
                            assertEquals(
                                requestDto.profileImage(),
                                member.getProfileImage(),
                                "프로필 이미지가 요청과 일치해야 합니다"),
                        () ->
                            assertEquals(
                                MemberRole.USER, member.getMemberRole(), "회원 역할은 USER여야 합니다"),
                        () -> assertEquals(Tier.UNRANK, member.getTier(), "티어는 UNRANK여야 합니다"));
                    return true;
                  }));
      verify(bCryptPasswordEncoder, times(1)).encode(requestDto.password());
    }
  }

  @Nested
  @DisplayName("로그인 시")
  class login {

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

    @Test
    @DisplayName("이메일과 비밀번호를 통해 회원 정보와 토큰을 반환합니다.")
    void login_Success() {
      // give
      String correctPassword = requestDto.password();

      when(testMember.getPassword().isSamePassword(correctPassword, bCryptPasswordEncoder))
          .thenReturn(true);
      when(jwtProvider.generateAccessToken(testMember.getId())).thenReturn(testAccessToken);
      when(jwtProvider.generateRefreshToken(testMember.getId())).thenReturn(testRefreshToken);

      // when
      LoginWithRefreshResponseDto responseDto =
          authCommandService.login(testMember, correctPassword);

      // then
      assertNotNull(responseDto, "응답이 null이면 안 됩니다");
      assertAll(
          "로그인 응답 속성 검증",
          () -> assertEquals(testMember.getId(), responseDto.memberId(), "회원 ID가 일치해야 합니다"),
          () -> assertEquals(testMember.getNickname(), responseDto.nickname(), "닉네임이 일치해야 합니다"),
          () ->
              assertEquals(
                  testMember.getProfileImage(), responseDto.profileImage(), "프로필 이미지가 일치해야 합니다"),
          () -> assertEquals(testAccessToken, responseDto.accessToken(), "액세스 토큰이 일치해야 합니다"),
          () -> assertEquals(testRefreshToken, responseDto.refreshToken(), "리프레시 토큰이 일치해야 합니다"));
      verify(jwtProvider, times(1)).generateAccessToken(testMember.getId());
      verify(jwtProvider, times(1)).generateRefreshToken(testMember.getId());
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 예외를 발생시킵니다.")
    void login_Fail() {
      // give
      String wrongPassword = testPassword;

      when(testMember.getPassword().isSamePassword(wrongPassword, bCryptPasswordEncoder))
          .thenReturn(false);

      // when
      AuthException exception =
          assertThrows(
              AuthException.class, () -> authCommandService.login(testMember, wrongPassword));

      // then
      assertEquals(
          GlobalErrorCode.PASSWORD_MISMATCH,
          exception.getErrorCode(),
          "에러 코드는 NOT_VALID_PASSWORD이어야 합니다");
      verify(jwtProvider, never()).generateAccessToken(anyLong());
      verify(jwtProvider, never()).generateRefreshToken(anyLong());
    }
  }

  @Nested
  @DisplayName("토큰을 재발급 받을 때")
  class refresh {
    @Test
    @DisplayName("유효한 refreshToken을 통해 재발급 받습니다.")
    void refresh_Success() {
      // give
      HttpServletRequest request = mock(HttpServletRequest.class);
      when(jwtProvider.extractRefreshToken(request)).thenReturn(Optional.of(testRefreshToken));
      when(jwtProvider.getSubject(testRefreshToken)).thenReturn(testId);
      when(jwtProvider.generateAccessToken(testId)).thenReturn(testAccessToken);
      when(jwtProvider.generateRefreshToken(testId)).thenReturn(testRefreshToken);

      // when
      RefreshResponseDto responseDto = authCommandService.refresh(request);

      // then
      assertNotNull(responseDto, "응답이 null이면 안 됩니다");
      assertAll(
          "토큰 갱신 응답 속성 검증",
          () -> assertEquals(testAccessToken, responseDto.accessToken(), "액세스 토큰이 일치해야 합니다"),
          () -> assertEquals(testRefreshToken, responseDto.refreshToken(), "리프레시 토큰이 일치해야 합니다"));
      verify(jwtProvider, times(1)).extractRefreshToken(request);
      verify(jwtProvider, times(1)).getSubject(testRefreshToken);
      verify(jwtProvider, times(1)).generateAccessToken(testId);
      verify(jwtProvider, times(1)).generateRefreshToken(testId);
    }

    @Test
    @DisplayName("토큰이 유효하지 않으면 예외를 발생시킵니다.")
    void refresh_Fail_Invalid_Token() {
      // give
      HttpServletRequest request = mock(HttpServletRequest.class);
      when(jwtProvider.extractRefreshToken(request)).thenReturn(Optional.empty());

      // when
      AuthException exception =
          assertThrows(
              AuthException.class,
              () -> authCommandService.refresh(request),
              "유효하지 않은 토큰으로 예외가 발생해야 합니다");

      // then
      assertEquals(
          GlobalErrorCode.INVALID_TOKEN, exception.getErrorCode(), "에러 코드는 INVALID_TOKEN이어야 합니다");
      verify(jwtProvider, times(1)).extractRefreshToken(request);
      verify(jwtProvider, never()).getSubject(anyString());
      verify(jwtProvider, never()).generateAccessToken(anyLong());
      verify(jwtProvider, never()).generateRefreshToken(anyLong());
    }
  }
}
