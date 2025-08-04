package com.example.notfound_backend.controller.login;

import com.example.notfound_backend.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class KakaoLoginController {

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    private final String KAKAO_AUTH_URL = "https://kauth.kakao.com/oauth/authorize";
    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USERINFO_URL = "https://kapi.kakao.com/v2/user/me";

    /**
     * Step 1: 카카오 로그인 페이지로 리다이렉트
     */
    @GetMapping("/kakao")
    public ResponseEntity<?> redirectToKakaoLogin() {
        String authorizationUrl = KAKAO_AUTH_URL +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code";

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, authorizationUrl)
                .build();
    }

    /**
     * Step 2: 카카오 로그인 콜백 처리
     */
    @GetMapping("/login/oauth2/code/kakao")
    public void handleKakaoCallback(@RequestParam String code, HttpServletResponse response) throws IOException {

        // 1️⃣ 카카오 토큰 요청
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String tokenRequestBody = "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&redirect_uri=" + redirectUri +
                "&code=" + code;

        ResponseEntity<Map> tokenResponse = restTemplate.exchange(
                KAKAO_TOKEN_URL, HttpMethod.POST,
                new HttpEntity<>(tokenRequestBody, tokenHeaders),
                Map.class
        );

        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            response.sendRedirect("/oauth2/fail?reason=token_error");
            return;
        }

        String kakaoAccessToken = (String) tokenResponse.getBody().get("access_token");

        // 2️⃣ 사용자 정보 요청
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(kakaoAccessToken);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                KAKAO_USERINFO_URL, HttpMethod.GET,
                new HttpEntity<>(userHeaders), Map.class
        );

        if (!userInfoResponse.getStatusCode().is2xxSuccessful()) {
            response.sendRedirect("/oauth2/fail?reason=userinfo_error");
            return;
        }

        Map<String, Object> body = userInfoResponse.getBody();
        Long kakaoId = ((Number) body.get("id")).longValue();
        Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");
        String role = "ROLE_USER";
        String username = (email != null) ? email : "kakao_" + kakaoId;

        // 3️⃣ JWT 생성
        String access = jwtUtil.createToken("access", username, role, 60 * 10 * 1000L); // 10분
        String refresh = jwtUtil.createToken("refresh", username, role, 24 * 60 * 60 * 1000L); // 24시간

        // 4️⃣ RefreshToken 쿠키 설정
        ResponseCookie cookie = ResponseCookie.from("refresh", refresh)
                .httpOnly(true)
                .secure(false) // HTTPS라면 true 권장
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 5️⃣ 프론트로 리다이렉트 (JWT와 유저정보 전달)
        String redirectUrl = "http://404notfoundpage.duckdns.org/oauth2/success"
                + "?accessToken=" + access
                + "&username=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
                + "&nickname=" + URLEncoder.encode(nickname, StandardCharsets.UTF_8)
                + "&role=" + role;

        response.sendRedirect(redirectUrl);
    }
}
