package com.bitcodex.service.impl;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.bitcodex.entity.User;
import com.bitcodex.exception.JwtTokenExpiredException;
import com.bitcodex.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {
	
	private String secretKey = "";
	
	public JwtServiceImpl() {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = keyGen.generateKey();
			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String generateToken(User user) {
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", user.getRoles());
		claims.put("status", user.getStatus().getIsActive());

		String token = Jwts.builder()
			.claims().add(claims)
			.subject(user.getEmail())
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis()+60*60*1))
			.and()
			.signWith(getKey())
			.compact();
		
		return token;
	}

	private Key getKey() {

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public String extractUsername(String token) {
		Claims claims = extractAllClaims(token);
		return claims.getSubject();
	}
	
	public String role(String token) {
		Claims claims = extractAllClaims(token);
		String role = (String) claims.get("role");
		return role;
	}

	private Claims extractAllClaims(String token) {
		try {
		Claims claims = Jwts.parser().verifyWith(decyptKey(secretKey)).build().parseSignedClaims(token).getPayload();
		return claims;
		
		}
		catch (ExpiredJwtException e) {
			throw new JwtTokenExpiredException("Token is Expired");
		}
		catch (JwtException e) {
			throw new JwtTokenExpiredException("Invalid Jwt token");
		}
		catch (Exception e) {
			throw e;
		}
		
	}

	private SecretKey decyptKey(String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public Boolean validateToken(String token, UserDetails userDetails) {

		String username = extractUsername(token);
		Boolean isExpired = isTokenExpried(token);
		
		if(username.equalsIgnoreCase(userDetails.getUsername()) && !isExpired) {
			return true;
		}
		
		return false;
	}

	private Boolean isTokenExpried(String token) {
		Claims claims = extractAllClaims(token);
		Date expiryDate = claims.getExpiration();
		return expiryDate.before(new Date());
	}

}
