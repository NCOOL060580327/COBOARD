package com.example.demo.Member;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.member.dto.request.SignUpMemberRequestDto;
import com.example.demo.member.entity.Member;
import com.example.demo.member.entity.MemberRole;
import com.example.demo.member.entity.Tier;
import com.example.demo.member.entity.repository.MemberRepository;
import com.example.demo.member.service.command.AuthCommandService;

@ExtendWith(MockitoExtension.class)
public class AuthCommandServiceTest {

  @Mock private MemberRepository memberRepository;

  @Mock private BCryptPasswordEncoder bCryptPasswordEncoder;

  @InjectMocks private AuthCommandService authCommandService;

  private SignUpMemberRequestDto requestDto;

  @BeforeEach
  void setUp() {
    requestDto =
        new SignUpMemberRequestDto(
            MemberTestConst.TEST_EMAIL.getValue(),
            MemberTestConst.TEST_PASSWORD.getValue(),
            MemberTestConst.TEST_NICKNAME.getValue(),
            MemberTestConst.TEST_PROFILE_IMAGE.getValue());
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
}
