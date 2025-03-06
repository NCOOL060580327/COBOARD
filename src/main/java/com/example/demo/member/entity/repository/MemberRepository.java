package com.example.demo.member.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Boolean existsByEmail(String email);

  Optional<Member> findByEmail(String email);

  Optional<Member> findByIdentifiedCode(String identifiedCode);

  Optional<Member> findByNicknameAndIdentifiedCode(String nickname, String identifiedCode);
}
