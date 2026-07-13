package com.flashdrop.catalog.infrastructure.config;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Configuración de seguridad del catalog-service.
 * - GET /catalog/** → público (cualquiera puede ver el catálogo).
 * - POST /catalog/** → requiere JWT válido con rol "Restaurante".
 * - Actuator health/prometheus → público.
 * - Todo lo demás → denegado.
 *
 * Valida el JWT usando la clave pública RSA compartida con auth-service.
 */
@Configuration
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${jwt.public-key:}")
    private String publicKeyPem;

    @Value("${jwt.issuer:flashdrop-auth}")
    private String jwtIssuer;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Lectura del catálogo: público
                .requestMatchers(HttpMethod.GET, "/catalog/**").permitAll()
                // Health y métricas: público
                .requestMatchers("/actuator/health/**", "/actuator/info", "/actuator/prometheus").permitAll()
                .requestMatchers("/health").permitAll()
                // Pre-flight CORS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Escritura requiere autenticación (el filtro JWT valida el rol)
                .requestMatchers(HttpMethod.POST, "/catalog/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/catalog/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/catalog/**").authenticated()
                .anyRequest().denyAll()
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Filtro JWT que extrae el token del header Authorization,
     * verifica la firma con la clave pública RSA del auth-service,
     * y puebla el SecurityContext con los roles del usuario.
     */
    @Bean
    OncePerRequestFilter jwtAuthenticationFilter() {
        return new OncePerRequestFilter() {
            @Override
            @SuppressWarnings("unchecked")
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain)
                    throws java.io.IOException, jakarta.servlet.ServletException {

                String authHeader = request.getHeader("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    filterChain.doFilter(request, response);
                    return;
                }

                try {
                    String token = authHeader.substring(7);
                    PublicKey rsaPublicKey = loadPublicKey();

                    Claims claims = Jwts.parser()
                            .verifyWith(rsaPublicKey)
                            .requireIssuer(jwtIssuer)
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();

                    String userId = claims.getSubject();
                    List<String> roles = claims.get("roles", List.class);
                    if (roles == null) roles = List.of();

                    var authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .toList();

                    var authentication = new UsernamePasswordAuthenticationToken(
                            UUID.fromString(userId), null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } catch (Exception e) {
                    log.debug("JWT inválido: {}", e.getMessage());
                    // No seteamos autenticación → Spring Security devolverá 401/403 si es necesario
                }

                filterChain.doFilter(request, response);
            }

            private PublicKey loadPublicKey() throws Exception {
                String pem = publicKeyPem
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
                        .replaceAll("\\s", "");
                byte[] decoded = Base64.getDecoder().decode(pem);
                X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
                return KeyFactory.getInstance("RSA").generatePublic(spec);
            }
        };
    }
}
