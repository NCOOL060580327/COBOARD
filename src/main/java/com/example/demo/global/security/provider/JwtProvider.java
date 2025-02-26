package com.example.demo.global.security.provider;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.TokenException;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {

  private static final String ISSUER = "COBOARD";
  private final SecretKey secretKey;
  private final Long accessTokenValidityMilliseconds;
  private final Long refreshTokenValidityMilliseconds;
  private final JwtParser jwtParser;

  public JwtProvider(
      @Value("${jwt.secret}") final String secretKey,
      @Value("${jwt.access-token-validity}") final long accessTokenValidityMilliseconds,
      @Value("${jwt.refresh-token-validity}") final long refreshTokenValidityMilliseconds) {
    this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    this.accessTokenValidityMilliseconds = accessTokenValidityMilliseconds;
    this.refreshTokenValidityMilliseconds = refreshTokenValidityMilliseconds;
    this.jwtParser = Jwts.parserBuilder().setSigningKey(this.secretKey).build();
  }

  public String generateAccessToken(Long memberId) {
    return generateToken(memberId, accessTokenValidityMilliseconds);
  }

  public String generateRefreshToken(Long memberId) {
    return generateToken(memberId, refreshTokenValidityMilliseconds);
  }

  private String generateToken(Long memberId, long validityMilliseconds) {
    Claims claims = Jwts.claims();
    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime tokenValidity = now.plus(validityMilliseconds, ChronoUnit.MILLIS);
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(memberId.toString())
        .setIssuedAt(Date.from(now.toInstant()))
        .setExpiration(Date.from(tokenValidity.toInstant()))
        .setIssuer(ISSUER)
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public Long getSubject(String token) {
    return Long.valueOf(getClaims(token).getBody().getSubject());
  }

  private Jws<Claims> getClaims(String token) {
    return jwtParser.parseClaimsJws(token);
  }

  public LocalDateTime getExpiredAt(String token) {
    return getClaims(token)
        .getBody()
        .getExpiration()
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }

  public Boolean isValidToken(String token) {
    try {
      Jws<Claims> claims = getClaims(token);
      Date expiredDate = claims.getBody().getExpiration();
      log.debug("만료된 토큰 검증: {}", expiredDate);
      return expiredDate.after(new Date());
    } catch (ExpiredJwtException e) {
      log.warn("토큰 만료", e);
      throw new TokenException(GlobalErrorCode.TOKEN_EXPIRED);
    } catch (MalformedJwtException e) {
      log.warn("잘못된 토큰 형식", e);
      throw new TokenException(GlobalErrorCode.MALFORMED_TOKEN);
    } catch (UnsupportedJwtException | IllegalArgumentException e) {
      log.warn("유효하지 않은 토큰 형식", e);
      throw new TokenException(GlobalErrorCode.INVALID_TOKEN);
    }
  }
}
