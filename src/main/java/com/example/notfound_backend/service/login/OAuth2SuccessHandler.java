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
        String username = oAuth2User.getAttribute("email"); // 카카오/네이버 모두 email
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

        // 1️⃣ JWT 생성
        String accessToken = jwtUtil.createToken("access", username, role, 60 * 60 * 1000L); // 1시간
        String refreshToken = jwtUtil.createToken("refresh", username, role, 60 * 60 * 24 * 1000L); // 24시간

        // 2️⃣ Refresh Token → HttpOnly Cookie 저장
        Cookie cookie = new Cookie("refresh", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // HTTPS 권장
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);

        // 3️⃣ 프론트엔드로 리다이렉트 (AccessToken 전달)
        //    프론트에서 쿼리스트링으로 토큰을 받아 Redux persist에 저장
        String redirectUrl = "http://404notfoundpage.duckdns.org/oauth2/success?accessToken=" + accessToken;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
