package project.assay.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.assay.models.Customer;

import javax.crypto.SecretKey;
import java.util.Date;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.io.Decoders.BASE64;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.lang.System.currentTimeMillis;
import static java.util.Map.of;
import static java.util.concurrent.TimeUnit.MINUTES;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-ttl-minutes}")
    private long accessTtlMinutes;

    @Getter
    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        this.signingKey = hmacShaKeyFor(BASE64.decode(secret));
    }

    public String generateToken(Customer customer) {
        return builder()
                .subject(customer.getEmail())
                .claims(of("role", customer.getRole().name()))
                .issuedAt(new Date(currentTimeMillis()))
                .expiration(new Date(currentTimeMillis() + MINUTES.toMillis(accessTtlMinutes)))
                .signWith(signingKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
