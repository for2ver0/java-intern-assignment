package org.eun0.assignment.common.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.eun0.assignment.auth.enums.Role;
import org.eun0.assignment.common.exception.ResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static org.eun0.assignment.common.ErrorCode.*;

@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";

    private static final long TOKEN_EXPIRATION_TIME = 2 * 60 * 60 * 1000L; // 2시간

    @Value("${jwt.secret.key}")
    String secretKey;

    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Long userId, String username, String nickname, List<Role> roles) {
        Date now = new Date();

        List<String> roleNames = roles
                .stream()
                .map(Role::getAuthorityName)
                .toList();

        String jwt = Jwts.builder()
                .subject(userId.toString())
                .claim("username", username)
                .claim("nickname", nickname)
                .claim("roles", roleNames)
                .signWith(key)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + TOKEN_EXPIRATION_TIME))
                .compact();

        return BEARER_PREFIX + jwt;
    }

    public String substringTokenFrom(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (MalformedJwtException e) {
            throw ResponseException.of(INVALID_TOKEN_TYPE);
        } catch (ExpiredJwtException e) {
            throw ResponseException.of(EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw ResponseException.of(INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            throw ResponseException.of(TOKEN_EMPTY);
        } catch (Exception e) {
            throw ResponseException.of(TOKEN_PARSING_ERROR);
        }
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long extractUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Long.parseLong(claims.getSubject());
    }

    public String extractUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }

    public String extractNicknameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("nickname", String.class);
    }

    public List<String> extractRolesFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("roles", List.class);
    }

}
