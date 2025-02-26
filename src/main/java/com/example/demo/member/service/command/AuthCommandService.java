package com.example.demo.member.service.command;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.member.dto.request.SignUpMemberRequestDto;
import com.example.demo.member.entity.Member;
import com.example.demo.member.entity.MemberRole;
import com.example.demo.member.entity.Password;
import com.example.demo.member.entity.Tier;
import com.example.demo.member.entity.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandService {

  private final MemberRepository memberRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   * 회원가입 요청 데이터를 기반으로 새로운 회원 엔티티를 생성합니다.
   *
   * @param requestDto 회원가입 요청 데이터 (이메일, 비밀번호, 닉네임, 프로필 이미지 등 포함)
   * @param memberRole 회원의 역할 (예: USER, ADMIN 등)
   * @return 생성된 {@link Member} 엔티티
   */
  public Member createMember(SignUpMemberRequestDto requestDto, MemberRole memberRole) {
    return Member.builder()
        .email(requestDto.email())
        .password(Password.encryptPassword(requestDto.password(), bCryptPasswordEncoder))
        .nickname(requestDto.nickName())
        .identifiedCode(generateIdentifier(requestDto.nickName()))
        .profileImage(requestDto.profileImage())
        .memberRole(memberRole)
        .tier(Tier.UNRANK)
        .build();
  }

  /**
   * 주어진 닉네임을 기반으로 고유한 식별자를 생성합니다. 식별자는 닉네임과 UUID의 일부를 조합한 형태입니다.
   *
   * @param nickname 회원의 닉네임
   * @return 생성된 고유 식별자 (형식: nickname#8자리UUID)
   */
  private String generateIdentifier(String nickname) {
    return nickname + "#" + UUID.randomUUID().toString().substring(0, 8);
  }

  /**
   * 새로운 회원을 등록합니다. 주어진 요청 데이터를 기반으로 회원 엔티티를 생성하고 데이터베이스에 저장합니다.
   *
   * @param requestDto 회원가입 요청 데이터
   */
  public void signUpMember(SignUpMemberRequestDto requestDto) {
    memberRepository.save(createMember(requestDto, MemberRole.USER));
  }
}
