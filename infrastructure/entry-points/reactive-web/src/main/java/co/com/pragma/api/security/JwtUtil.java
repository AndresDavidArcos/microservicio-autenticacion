package co.com.pragma.api.security;


import co.com.pragma.secretsprovider.SecretsProvider;
import co.com.pragma.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private final long jwtExpirationMs;
    private final SecretsProvider secretsProvider;

    public JwtUtil(@Value("${adapters.jwt.expiration-ms}") long jwtExpirationMs, SecretsProvider secretsProvider) {
        this.jwtExpirationMs = jwtExpirationMs;
        this.secretsProvider = secretsProvider;
    }

/*
    public JwtUtil(@Value("${adapter.jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }
*/

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(user.getCorreoElectronico())
                .claim("rol", user.getRol())
                .claim("documentoIdentidad", user.getDocumentoIdentidad())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(secretsProvider.getJwtSecret().getBytes()))
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretsProvider.getJwtSecret().getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getRoleFromToken(String token) {
        return getAllClaimsFromToken(token).get("rol", String.class);
    }

    private Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    public String getDocumentoFromToken(String token) {
        return getAllClaimsFromToken(token).get("documentoIdentidad", String.class);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
