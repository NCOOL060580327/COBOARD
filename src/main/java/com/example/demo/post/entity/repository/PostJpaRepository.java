package com.example.demo.post.entity.repository;

import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.post.entity.Post;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(value = "SELECT p FROM Post p WHERE p.id = :id")
  Optional<Post> findByIdWithPessimisticLock(@Param("id") Long id);
}
