package com.example.notfound_backend.service.login;

import com.example.notfound_backend.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String username = oAuth2User.getAttribute("email"); // 카카오/네이버 모두 email 추출
        String role = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse("ROLE_USER");

        // JWT 생성
        String accessToken = jwtUtil.createToken("access", username, role, 60 * 60 * 1000L);
        String refreshToken = jwtUtil.createToken("refresh", username, role, 60 * 60 * 24 * 1000L);

        // 응답에 JWT 추가
        response.addHeader("Authorization", "Bearer " + accessToken);
        Cookie cookie = new Cookie("refresh", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);

        // 프론트엔드 페이지로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000");
    }
}