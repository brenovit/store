package io.github.brenovit.store.config;

import java.util.Base64;
import java.util.Date;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.github.brenovit.store.model.Role;
import io.github.brenovit.store.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JwtTokenProvider {
	@Value("${security.jwt.token.secret-key}")
	private String secretKey;

	@Value("${security.jwt.token.expire-length}")
	private long validityInMilliseconds;
	
	@Value("${security.jwt.token.authorization.key}")
	private String authorizationBearerKey;

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@PostConstruct
	protected void init() {
	    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	public String createToken(String username, Set<Role> set) {
	    Claims claims = Jwts.claims().setSubject(username);
	    claims.put("roles", set);
	    Date now = new Date();
	    Date validity = new Date(now.getTime() + validityInMilliseconds);
        log.info("Validation of token is: {}", validity.toString());
	    return Jwts.builder()//
	        .setClaims(claims)//
	        .setIssuedAt(now)//
	        .setExpiration(validity)//
	        .signWith(SignatureAlgorithm.HS256, secretKey)//
	        .compact();
	}
	
	public Authentication getAuthentication(String token) {
	    UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
	    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	public String getUsername(String token) {
	    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}
	
	public String resolveToken(HttpServletRequest req) {
	    String bearerToken = req.getHeader("Authorization");
	    if (bearerToken != null && bearerToken.startsWith(authorizationBearerKey)) {
	        return bearerToken.substring(authorizationBearerKey.length(), bearerToken.length());
	    }
	    return null;
	}
	
	public boolean validateToken(String token) {
	    try {
	    	if(StringUtils.isEmpty(token)) {
	    		return false;
	    	}
	        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
	        log.info("Expiration date is: {}", token, claims.getBody().getExpiration().toString());
	        if (claims.getBody().getExpiration().before(new Date())) {
	            return false;
	        }
	        return true;
	    } catch (JwtException | IllegalArgumentException e) {
	    	log.error(e.getMessage(), e);
	    }
	    return false;
	}
}
