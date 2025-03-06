package com.example.demo.member;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
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
import com.example.demo.member.dto.response.GetMemberByIdentifiedCodeResponseDto;
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
  private final String testIdentifiedCode = MemberTestConst.TEST_IDENTIFIED_CODE.getValue();

  @BeforeEach
  void setUp() {}

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

  @Nested
  @DisplayName("식별코드를 통해 사용자를 조회할 때")
  class GetMemberByIdentifiedCode {

    Member testMember =
        Member.builder()
            .id(testId)
            .email(testEmail)
            .password(new Password(testPassword))
            .nickname(testNickname)
            .profileImage(testProfileImage)
            .memberRole(MemberRole.USER)
            .tier(Tier.UNRANK)
            .identifiedCode(testIdentifiedCode)
            .build();

    @Test
    @DisplayName("사용자를 반환합니다.")
    void getMemberByIdentifiedCode_Success() {
      // given
      when(memberRepository.findByIdentifiedCode(testIdentifiedCode))
          .thenReturn(Optional.of(testMember));

      // when
      Member result = memberQueryService.getMemberByIdentifiedCode(testIdentifiedCode);

      // then
      assertNotNull(result, "반환된 사용자가 null이면 안됩니다");
      assertEquals(testMember, result, "반환된 값이 사용자와 일치해야합니다");
      verify(memberRepository, times(1)).findByIdentifiedCode(testIdentifiedCode);
    }

    @Test
    @DisplayName("사용자가 없으면 예외를 발생시킵니다.")
    void getMemberByIdentifiedCode_Fail_MEMBER_NOT_FOUND() {
      // given
      when(memberRepository.findByIdentifiedCode(testIdentifiedCode)).thenReturn(Optional.empty());

      // when
      MemberException exception =
          assertThrows(
              MemberException.class,
              () -> memberQueryService.getMemberByIdentifiedCode(testIdentifiedCode));

      // then
      assertEquals(GlobalErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
      verify(memberRepository, times(1)).findByIdentifiedCode(testIdentifiedCode);
    }
  }

  @Nested
  @DisplayName("ResponseDto 리스트를 통해 멤버 리스트를 조회할 때")
  class GetMemberListByIdentifiedCodeResponseDtoList {

    Member testMember1 =
        Member.builder()
            .id(testId)
            .email(testEmail)
            .password(new Password(testPassword))
            .nickname(testNickname)
            .profileImage(testProfileImage)
            .memberRole(MemberRole.USER)
            .tier(Tier.UNRANK)
            .identifiedCode(testIdentifiedCode)
            .build();

    Member testMember2 =
        Member.builder()
            .id(testId + 1)
            .email(testEmail + "2")
            .password(new Password(testPassword))
            .nickname(testNickname + "2")
            .profileImage(testProfileImage)
            .memberRole(MemberRole.USER)
            .tier(Tier.UNRANK)
            .identifiedCode(testIdentifiedCode + "2")
            .build();

    @Test
    @DisplayName("멤버 리스트를 성공적으로 반환합니다.")
    void getMemberList_Success() {
      // given
      List<GetMemberByIdentifiedCodeResponseDto> responseDtoList =
          List.of(
              new GetMemberByIdentifiedCodeResponseDto(testNickname, testIdentifiedCode),
              new GetMemberByIdentifiedCodeResponseDto(
                  testNickname + "2", testIdentifiedCode + "2"));

      when(memberRepository.findByNicknameAndIdentifiedCode(testNickname, testIdentifiedCode))
          .thenReturn(Optional.of(testMember1));
      when(memberRepository.findByNicknameAndIdentifiedCode(
              testNickname + "2", testIdentifiedCode + "2"))
          .thenReturn(Optional.of(testMember2));

      // when
      List<Member> result =
          memberQueryService.getMemberListByIdentifiedCodeResponseDtoList(responseDtoList);

      // then
      assertNotNull(result, "반환된 멤버 리스트가 null이면 안됩니다");
      assertEquals(2, result.size(), "리스트 크기가 요청한 DTO 수와 일치해야 합니다");
      assertTrue(result.contains(testMember1), "testMember1이 포함되어야 합니다");
      assertTrue(result.contains(testMember2), "testMember2가 포함되어야 합니다");
      verify(memberRepository, times(1))
          .findByNicknameAndIdentifiedCode(testNickname, testIdentifiedCode);
      verify(memberRepository, times(1))
          .findByNicknameAndIdentifiedCode(testNickname + "2", testIdentifiedCode + "2");
    }

    @Test
    @DisplayName("일부 멤버가 없어도 유효한 멤버만 반환합니다.")
    void getMemberList_PartialSuccess() {
      // given
      List<GetMemberByIdentifiedCodeResponseDto> responseDtoList =
          List.of(
              new GetMemberByIdentifiedCodeResponseDto(testNickname, testIdentifiedCode),
              new GetMemberByIdentifiedCodeResponseDto("unknown", "UNKNOWN_CODE"));

      when(memberRepository.findByNicknameAndIdentifiedCode(testNickname, testIdentifiedCode))
          .thenReturn(Optional.of(testMember1));
      when(memberRepository.findByNicknameAndIdentifiedCode("unknown", "UNKNOWN_CODE"))
          .thenReturn(Optional.empty());

      // when
      List<Member> result =
          memberQueryService.getMemberListByIdentifiedCodeResponseDtoList(responseDtoList);

      // then
      assertNotNull(result, "반환된 멤버 리스트가 null이면 안됩니다");
      assertEquals(1, result.size(), "유효한 멤버만 포함해야 합니다");
      assertEquals(testMember1, result.getFirst(), "testMember1만 포함되어야 합니다");
      verify(memberRepository, times(1))
          .findByNicknameAndIdentifiedCode(testNickname, testIdentifiedCode);
      verify(memberRepository, times(1)).findByNicknameAndIdentifiedCode("unknown", "UNKNOWN_CODE");
    }
  }
}
