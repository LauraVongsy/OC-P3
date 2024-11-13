package utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtils {
    private static final String SECRET_KEY = "368900434251d7539a00042a31ba81d4f33906fc68a2dcb49d0e4b9e03f04a22";

    // Méthode pour générer un token JWT
    public static String generateToken(String email) {
        long expirationTime = 86400000; // 1 jour en millisecondes

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Méthode pour valider un token JWT et extraire les informations de l'utilisateur
    public static Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // Nouvelle méthode pour configurer l'authentification dans Spring Security
    public static void setAuthenticationFromToken(String token, HttpServletRequest request) {
        Claims claims = validateToken(token);

        if (claims != null) {
            String email = claims.getSubject();
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    email, null, null);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }
}
