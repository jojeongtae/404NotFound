package com.example.notfound_backend.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtLoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.setFilterProcessesUrl("/api/login"); // 로그인경로 커스텀
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username;
        String password;

        // Content-Type에 따라 요청 본문 파싱 방식 변경
        if (request.getContentType() != null && request.getContentType().contains("application/json")) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> loginRequest = objectMapper.readValue(request.getInputStream(), Map.class);
                username = loginRequest.get("username");
                password = loginRequest.get("password");
            } catch (IOException e) {
                throw new AuthenticationServiceException("Error parsing login request JSON", e);
            }
        } else {
            // 기본 Form Data 파싱
            username = obtainUsername(request);
            password = obtainPassword(request);
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password, null);
        return authenticationManager.authenticate(authRequest);
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                         Authentication authResult) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        String username = userDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority grantedAuthority = iterator.next();
        String role = grantedAuthority.getAuthority();
        Map<String , Object> responseData = new HashMap<>();
        responseData.put("role", role);
        responseData.put("result", "로그인 성공");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(responseData);
        // 토큰생성

        String access_token = this.jwtUtil.createToken("access", username, role, 60 * 60 * 1000L); // 1시간
        String refresh_token = this.jwtUtil.createToken("refresh", username, role, 60 * 60 * 24 * 1000L); // 24일
        response.addHeader("Authorization", "Bearer " + access_token);
        response.addCookie(this.createCookie("refresh", refresh_token));

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(json);


    }
    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("error", "로그인 실패");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonmessage = objectMapper.writeValueAsString(responseData);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonmessage);
    }
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/"); // 루트 경로부터 시작되는 모든 요청에 대해 쿠키가 포함되도록 설정
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24);
        return cookie;
    }

}
