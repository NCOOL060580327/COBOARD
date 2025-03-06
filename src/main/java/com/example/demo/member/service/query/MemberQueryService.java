package com.example.demo.member.service.query;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.global.annotation.ReadOnlyTransactional;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.MemberException;
import com.example.demo.member.dto.response.GetMemberByIdentifiedCodeResponseDto;
import com.example.demo.member.entity.Member;
import com.example.demo.member.entity.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ReadOnlyTransactional
public class MemberQueryService {

  private final MemberRepository memberRepository;

  /**
   * 주어진 이메일로 회원을 조회합니다.
   *
   * @param email 조회할 회원의 이메일
   * @return 조회된 {@link Member} 엔티티
   * @throws MemberException {@code GlobalErrorCode.MEMBER_NOT_FOUND} - 해당 이메일로 회원을 찾을 수 없을 경우
   */
  public Member getMemberByEmail(String email) {
    return memberRepository
        .findByEmail(email)
        .orElseThrow(() -> new MemberException(GlobalErrorCode.MEMBER_NOT_FOUND));
  }

  /**
   * 주어진 identifiedCode로 회원을 조회합니다.
   *
   * @param identifiedCode 조회할 회원의 고유 식별 코드
   * @return 조회된 {@link Member} 엔티티
   * @throws MemberException {@code GlobalErrorCode.MEMBER_NOT_FOUND} - 해당 identifiedCode로 회원을 찾을 수
   *     없을 경우
   */
  public Member getMemberByIdentifiedCode(String identifiedCode) {
    return memberRepository
        .findByIdentifiedCode(identifiedCode)
        .orElseThrow(() -> new MemberException(GlobalErrorCode.MEMBER_NOT_FOUND));
  }

  /**
   * 주어진 {@link GetMemberByIdentifiedCodeResponseDto} 리스트에서 회원 목록을 조회합니다. 리스트가 {@code null}인 경우 빈
   * 리스트를 반환하며, 조회되지 않는 회원은 결과에서 제외됩니다.
   *
   * @param responseDtoList 회원 정보를 포함한 {@link GetMemberByIdentifiedCodeResponseDto} 리스트
   * @return 조회된 {@link Member} 엔티티 리스트. 조회된 회원이 없으면 빈 리스트 반환
   */
  public List<Member> getMemberListByIdentifiedCodeResponseDtoList(
      List<GetMemberByIdentifiedCodeResponseDto> responseDtoList) {
    return Optional.ofNullable(responseDtoList).orElseGet(Collections::emptyList).stream()
        .map(
            responseDto ->
                memberRepository.findByNicknameAndIdentifiedCode(
                    responseDto.nickname(), responseDto.identifiedCode()))
        .flatMap(Optional::stream)
        .toList();
  }
}
