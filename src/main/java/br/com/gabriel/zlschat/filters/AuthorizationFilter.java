package br.com.gabriel.zlschat.filters;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthorizationFilter extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Cookie[] cookies = request.getCookies();
            String jwtToken = null;
            for (Cookie cookie : cookies) {
                jwtToken = cookie.getName().equals("sessiontoken") ? cookie.getValue() : jwtToken;
            }

            if (jwtToken != null) {
                Jws<Claims> claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(jwtToken);

                    
                    String username = claims.getPayload().get("username", String.class);

                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, null));
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            ObjectMapper ObjectMapper = new ObjectMapper();
            Map<String, Object> errorInfo = new HashMap<String, Object>();
            errorInfo.put("timestamp", LocalDateTime.now().toString());
            errorInfo.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            errorInfo.put("error", "Nenhuma autorização encontrada");
            errorInfo.put("path", request.getRequestURI());
            errorInfo.put("message", "Nenhuma autorização encontrada");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.addHeader("content-type", "application/json;charset=UTF-8");
            response.getWriter().write(ObjectMapper.writeValueAsString(errorInfo));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().equalsIgnoreCase("/api/user/login")
            || request.getRequestURI().equalsIgnoreCase("/api/user/register")
            || request.getRequestURI().contains("/api/user/logout");
    }
}
