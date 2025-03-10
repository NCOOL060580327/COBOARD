package com.example.demo.post.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.post.entity.Post;

public interface PostJpaRepository extends JpaRepository<Post, Long> {}
