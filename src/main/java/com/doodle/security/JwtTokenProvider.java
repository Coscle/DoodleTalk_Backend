package com.doodle.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.doodle.entity.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    // 토큰 유효시간 2시간
    private long tokenValidTime = 2 * 60 * 60 * 1000L;

    private final UserDetailsService userDetailsService;
    
    private Key key; // secretKey를 Key 객체로 변환

    @PostConstruct
    protected void init() {
    	// Base64로 인코딩된 secretKey를 디코딩하여 Key 객체로 변환
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);    }

    // JWT 토큰 생성
    public String createToken(String email, String nickname, Role roles) {
        Claims claims = Jwts.claims().setSubject(email); // JWT payload에 저장되는 정보 단위
        claims.put("nickname", nickname); // 정보 저장 (key-value)
        claims.put("roles", roles); // 정보 저장 (key-value)
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(key, SignatureAlgorithm.HS256) // 사용할 암호화 알고리즘과 signature에 들어갈 key 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPK(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPK(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
        return parser.parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져온다. "X-AUTH-TOKEN": "TOKEN 값"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
            return !parser.parseClaimsJws(jwtToken).getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
