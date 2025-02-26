package com.example.demo.Member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
import com.example.demo.member.entity.repository.MemberRepository;
import com.example.demo.member.service.query.MemberQueryService;

@ExtendWith(MockitoExtension.class)
public class MemberQueryServiceTest {

  @Mock private MemberRepository memberRepository;

  @InjectMocks private MemberQueryService memberQueryService;

  private String testEmail;

  @BeforeEach
  void setUp() {
    testEmail = MemberTestConst.TEST_EMAIL.getValue();
  }

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
}
