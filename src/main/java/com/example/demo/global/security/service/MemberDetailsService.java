package com.example.demo.global.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.global.security.domain.MemberDetails;
import com.example.demo.member.entity.Member;
import com.example.demo.member.entity.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {

    Member member =
        memberRepository
            .findById(Long.parseLong(memberId))
            .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다: " + memberId));

    return new MemberDetails(member);
  }
}
