package br.com.gabriel.zlschat.filters;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.gabriel.zlschat.dtos.LoginCredentialsDTO;
import br.com.gabriel.zlschat.models.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final String secretKey;
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LoginCredentialsDTO loginCredentialsDTO = objectMapper.readValue(request.getInputStream(), LoginCredentialsDTO.class);
            String email = loginCredentialsDTO.getEmail();
            String password = loginCredentialsDTO.getPassword();
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
            return this.authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar credenciais de login");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserEntity user = (UserEntity) authResult.getPrincipal();
        String jwtToken = Jwts.builder()
            .subject(user.getEmail())
            .issuer("zlschat")
            .claim("username", user.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(this.daysToMilliseconds(7)))
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), Jwts.SIG.HS256)
            .compact();
        Cookie jwtCookie = new Cookie("sessiontoken", jwtToken);
        jwtCookie.setMaxAge(this.daysToSeconds(7));
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> errorInfo = new HashMap<String, Object>();
        errorInfo.put("timestamp", LocalDateTime.now().toString());
        errorInfo.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        errorInfo.put("error", "Email e/ou senha inválida");
        errorInfo.put("path", request.getRequestURI());
        errorInfo.put("message", failed.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorInfo));
    }

    private long daysToMilliseconds(int days) {
        return System.currentTimeMillis() + 1000 * 60 * 60 * 24 * days;
    }

    private int daysToSeconds(int days) {
        return 60 * 60 * 24 * days;
    }
}
