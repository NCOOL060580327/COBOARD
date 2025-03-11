package com.example.demo.post.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.board.entity.BoardMember;
import com.example.demo.post.entity.Post;
import com.example.demo.post.entity.PostLike;

public interface PostLikeJpaRepository extends JpaRepository<PostLike, Long> {
  Optional<PostLike> findPostLikeByPostAndBoardMember(Post post, BoardMember boardMember);

  Boolean existsByPostAndBoardMember(Post post, BoardMember boardMember);
}
