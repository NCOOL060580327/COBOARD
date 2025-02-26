package com.example.demo.member.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.MemberException;
import com.example.demo.member.entity.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {

  private final MemberRepository memberRepository;

  /**
   * 주어진 이메일이 중복되지 않는지 확인합니다.
   *
   * @param email 확인할 이메일 주소
   */
  public void isValidEmail(String email) {
    if (memberRepository.existsByEmail(email)) {
      throw new MemberException(GlobalErrorCode.DUPLICATE_EMAIL);
    }
  }
}
