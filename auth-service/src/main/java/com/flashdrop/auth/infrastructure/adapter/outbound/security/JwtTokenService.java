package com.flashdrop.auth.infrastructure.adapter.outbound.security;

import com.flashdrop.auth.application.dto.TokenClaims;
import com.flashdrop.auth.application.port.outbound.TokenService;
import com.flashdrop.auth.domain.exception.InvalidTokenException;
import com.flashdrop.auth.infrastructure.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Emite y verifica JWT firmados con RS256 (clave asimétrica). El gateway puede
 * validar con la clave pública publicada en el JWKS sin conocer la privada.
 */
@Component
public class JwtTokenService implements TokenService {

    private final RsaKeyProvider keys;
    private final String issuer;
    private final long accessTtlSeconds;

    public JwtTokenService(RsaKeyProvider keys, JwtProperties props) {
        this.keys = keys;
        this.issuer = props.getIssuer();
        this.accessTtlSeconds = props.getAccessExpirationMinutes() * 60;
    }

    @Override
    public String issue(TokenClaims claims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .header().keyId(keys.kid()).and()
                .issuer(issuer)
                .subject(claims.userId().toString())
                .claim("email", claims.email())
                .claim("roles", claims.roles())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTtlSeconds)))
                .signWith(keys.privateKey(), Jwts.SIG.RS256)
                .compact();
    }

    @Override
    @SuppressWarnings("unchecked")
    public TokenClaims verify(String token) {
        try {
            Claims c = Jwts.parser()
                    .verifyWith(keys.publicKey())
                    .requireIssuer(issuer)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            List<String> roles = c.get("roles", List.class);
            return new TokenClaims(UUID.fromString(c.getSubject()),
                    c.get("email", String.class), roles == null ? List.of() : roles);
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    @Override
    public long accessTtlSeconds() {
        return accessTtlSeconds;
    }
}
