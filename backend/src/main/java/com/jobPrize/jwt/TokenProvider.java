package com.jobPrize.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jobPrize.enumerate.ApprovalStatus;
import com.jobPrize.enumerate.UserType;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider {

	private final Key key;

	public TokenProvider(@Value("${jwt.secret}") String secret) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public String createAccessToken(Long id, UserType userType, ApprovalStatus approvalStatus, boolean isSubscribed) { // 고유 식별자, 권한, 만료시간(프로젝트때 수정)
		Date now = new Date();
		Date expiry = new Date(now.getTime()+1000*60*60*2);

		return Jwts.builder()
				.setSubject(String.valueOf(id)) // subject를 id로
				.claim("userType", userType.name())	// claim에 유저타입 추가
				.claim("approvalState", approvalStatus.name()) // claim에 승인여부 추가
				.claim("isSubscribe", isSubscribed) // claim에 구독여부 추가
				.setIssuedAt(now).setExpiration(expiry).signWith(key, SignatureAlgorithm.HS256).compact();
	}
	
	public String createRefreshToken(Long id, UserType userType, ApprovalStatus approvalStatus, boolean isSubscribed) { // 고유 식별자, 권한, 만료시간(프로젝트때 수정)
		Date now = new Date();
		Date expiry = new Date(now.getTime()+1000*60*60*24*14);

		return Jwts.builder()
				.setSubject(String.valueOf(id)) // subject를 id로
				.claim("userType", userType.name())	// claim에 유저타입 추가
				.claim("approvalState", approvalStatus.name()) // claim에 승인여부 추가
				.claim("isSubscribe", isSubscribed) // claim에 구독여부 추가
				.setIssuedAt(now).setExpiration(expiry).signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException
				| IllegalArgumentException e) {
			return false;
		}
	}

	public Long getIdFromToken(String token) {
		return Long.valueOf(Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject());
	}
	
	public UserType getUserTypeFromToken(String token) {
		return UserType.valueOf(Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("userType", String.class));
	}
}