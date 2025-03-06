package com.example.demo.global.security.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.MemberException;
import com.example.demo.global.exception.custom.TokenException;
import com.example.demo.global.security.domain.MemberDetails;
import com.example.demo.global.security.provider.JwtProvider;
import com.example.demo.global.security.service.MemberDetailsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

  private static final String BEARER_PREFIX = "Bearer ";

  private final JwtProvider jwtProvider;
  private final MemberDetailsService memberDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authorizationHeader = request.getHeader("Authorization");

    if (hasValidBearerToken(authorizationHeader)) {
      String token = authorizationHeader.substring(BEARER_PREFIX.length());
      processToken(token);
    }
    filterChain.doFilter(request, response);
  }

  private boolean hasValidBearerToken(String authorizationHeader) {
    return authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX);
  }

  private void processToken(String token) {

    if (!jwtProvider.isValidToken(token)) {
      log.warn("유효하지 않은 JWT 토큰: {}", token);
      throw new TokenException(GlobalErrorCode.INVALID_TOKEN);
    }

    Long userId = jwtProvider.getSubject(token);
    MemberDetails userDetails = memberDetailsService.loadUserByUsername(userId.toString());

    if (userDetails == null) {
      log.error("userId에 해당하는 사용자를 찾을 수 없습니다.: {}", userId);
      throw new MemberException(GlobalErrorCode.MEMBER_NOT_FOUND);
    }

    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authToken);
    log.debug("Authentication set for userId: {}", userId);
  }

  //  @Override
  //  protected boolean shouldNotFilter(HttpServletRequest request) {
  //    String path = request.getRequestURI();
  //    return path.startsWith("");
  //  }

}
