package com.example.demo.member.service.command;

import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.AuthException;
import com.example.demo.global.security.provider.JwtProvider;
import com.example.demo.member.dto.request.SignUpMemberRequestDto;
import com.example.demo.member.dto.response.LoginResponseDto;
import com.example.demo.member.dto.response.LoginWithRefreshResponseDto;
import com.example.demo.member.dto.response.RefreshResponseDto;
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
  private final JwtProvider jwtProvider;

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

  /**
   * 주어진 회원과 비밀번호로 로그인 처리를 수행합니다.
   *
   * @param member 로그인 대상 회원 엔티티
   * @param password 입력된 비밀번호
   * @return 로그인 성공 시 생성된 {@link LoginResponseDto} (액세스 토큰과 리프레시 토큰 포함)
   */
  public LoginWithRefreshResponseDto login(Member member, String password) {

    if (!(member.getPassword().isSamePassword(password, bCryptPasswordEncoder))) {
      throw new AuthException(GlobalErrorCode.PASSWORD_MISMATCH);
    }

    String accessToken = jwtProvider.generateAccessToken(member.getId());
    String refreshToken = jwtProvider.generateRefreshToken(member.getId());

    return LoginWithRefreshResponseDto.from(member, accessToken, refreshToken);
  }

  public RefreshResponseDto refresh(HttpServletRequest request) {

    String refreshToken =
        jwtProvider
            .extractRefreshToken(request)
            .orElseThrow(() -> new AuthException(GlobalErrorCode.INVALID_TOKEN));

    Long memberId = jwtProvider.getSubject(refreshToken);

    String newAccessToken = jwtProvider.generateAccessToken(memberId);
    String newRefreshToken = jwtProvider.generateRefreshToken(memberId);

    return new RefreshResponseDto(newAccessToken, newRefreshToken);
  }
}
