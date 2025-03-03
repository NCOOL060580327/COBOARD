package com.example.demo.Member;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.MemberException;
import com.example.demo.member.entity.Member;
import com.example.demo.member.entity.MemberRole;
import com.example.demo.member.entity.Password;
import com.example.demo.member.entity.Tier;
import com.example.demo.member.entity.repository.MemberRepository;
import com.example.demo.member.service.query.MemberQueryService;

@ExtendWith(MockitoExtension.class)
public class MemberQueryServiceTest {

  @Mock private MemberRepository memberRepository;

  @InjectMocks private MemberQueryService memberQueryService;

  private final Long testId = Long.parseLong(MemberTestConst.TEST_ID.getValue());
  private final String testEmail = MemberTestConst.TEST_EMAIL.getValue();
  private final String testPassword = MemberTestConst.TEST_PASSWORD.getValue();
  private final String testNickname = MemberTestConst.TEST_NICKNAME.getValue();
  private final String testProfileImage = MemberTestConst.TEST_PROFILE_IMAGE.getValue();

  @BeforeEach
  void setUp() {}

  @Nested
  @DisplayName("이메일 중복 확인 시")
  class isValidEmail {

    @Test
    @DisplayName("중복되는 메일이 있는지 확인합니다.")
    void isValidEmail_Success() {
      // give
      when(memberRepository.existsByEmail(testEmail)).thenReturn(false);

      // when
      memberQueryService.isValidEmail(testEmail);

      // then
      verify(memberRepository, times(1)).existsByEmail(testEmail);
    }

    @Test
    @DisplayName("중복될 경우 예외를 발생시킵니다.")
    void isValidEmail_Fail() {
      // give
      when(memberRepository.existsByEmail(testEmail)).thenReturn(true);

      // when
      MemberException exception =
          assertThrows(MemberException.class, () -> memberQueryService.isValidEmail(testEmail));

      // then
      verify(memberRepository, times(1)).existsByEmail(testEmail);
      assertEquals(GlobalErrorCode.DUPLICATE_EMAIL, exception.getErrorCode());
    }
  }

  @Nested
  @DisplayName("이메일을 통해 사용자를 조회할 때")
  class getMemberByEmail {

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
    @DisplayName("사용자를 반환합니다.")
    void getMemberByEmail_Success() {
      // give
      when(memberRepository.findByEmail(testEmail)).thenReturn(Optional.ofNullable(testMember));

      // when
      Member result = memberQueryService.getMemberByEmail(testEmail);

      // then
      assertNotNull(result, "반환된 사용자가 null이면 안됩니다");
      assertEquals(testMember, result, "반환된 값이 사용자와 일치해야합니다");
      verify(memberRepository, times(1)).findByEmail(testEmail);
    }

    @Test
    @DisplayName("사용자가 없으면 예외를 발생시킵니다.")
    void getMemberByEmail_Fail() {
      // give
      when(memberRepository.findByEmail(testEmail)).thenReturn(Optional.empty());

      // when
      MemberException exception =
          assertThrows(MemberException.class, () -> memberQueryService.getMemberByEmail(testEmail));

      // then
      assertEquals(GlobalErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
      verify(memberRepository, times(1)).findByEmail(testEmail);
    }
  }
}
