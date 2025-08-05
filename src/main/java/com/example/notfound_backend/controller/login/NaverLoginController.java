package com.example.notfound_backend.controller.login;

import com.example.notfound_backend.jwt.JwtUtil;
import com.example.notfound_backend.jwt.JwtUtil;
import com.example.notfound_backend.service.OAuthUserService;
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
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class NaverLoginController {

    private final OAuthUserService oAuthUserService;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirectUri;

    private final String NAVER_AUTH_URL = "https://nid.naver.com/oauth2.0/authorize";
    private final String NAVER_TOKEN_URL = "https://nid.naver.com/oauth2.0/token";
    private final String NAVER_USERINFO_URL = "https://openapi.naver.com/v1/nid/me";

    /**
     * Step 1: 네이버 로그인 페이지로 리다이렉트
     */
    @GetMapping("/naver")
    public ResponseEntity<?> redirectToNaverLogin() {
        String state = UUID.randomUUID().toString();
        String authorizationUrl = NAVER_AUTH_URL +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&state=" + state;

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, authorizationUrl)
                .build();
    }

    /**
     * Step 2: 네이버 로그인 콜백 처리
     */
    @GetMapping("/login/oauth2/code/naver")
    public void handleNaverCallback(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws IOException {

        // 1️⃣ 네이버 토큰 요청
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String tokenRequestBody = "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&redirect_uri=" + redirectUri +
                "&code=" + code +
                "&state=" + state;

        ResponseEntity<Map> tokenResponse = restTemplate.exchange(
                NAVER_TOKEN_URL, HttpMethod.POST,
                new HttpEntity<>(tokenRequestBody, tokenHeaders),
                Map.class
        );

        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            response.sendRedirect("/oauth2/fail?reason=token_error");
            return;
        }

        String naverAccessToken = (String) tokenResponse.getBody().get("access_token");

        // 2️⃣ 사용자 정보 요청
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.set("Authorization", "Bearer " + naverAccessToken);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                NAVER_USERINFO_URL, HttpMethod.GET,
                new HttpEntity<>(userHeaders), Map.class
        );

        if (!userInfoResponse.getStatusCode().is2xxSuccessful()) {
            response.sendRedirect("/oauth2/fail?reason=userinfo_error");
            return;
        }

        Map<String, Object> body = userInfoResponse.getBody();
        Map<String, Object> naverResponse = (Map<String, Object>) body.get("response");

        // ✅ Null 안전 처리
        String email = (String) naverResponse.get("email");

        // 네이버는 nickname과 name을 모두 제공할 수 있으므로, nickname을 우선 사용하고 없으면 name을 사용
        Object nicknameObj = naverResponse.get("nickname");
        Object nameObj = naverResponse.get("name");
        String nickname = "네이버유저"; // 기본값

        if (nicknameObj != null) {
            nickname = nicknameObj.toString();
        } else if (nameObj != null) {
            nickname = nameObj.toString();
        }

        String role = "ROLE_USER";
        String username = (email != null) ? email : "naver_" + naverResponse.get("id").toString();
        String phone = (String) naverResponse.getOrDefault("mobile", "010-0000-0000");
        String address = "주소 미등록"; // 네이버는 주소 정보 제공 안함

        // 3️⃣ 사용자 정보 처리 (OAuthUserService 위임)
        oAuthUserService.processOAuthUser(username, role, nickname, phone, address);

        // 4️⃣ JWT 생성
        String access = jwtUtil.createToken("access", username, role, 60 * 10 * 1000L); // 10분
        String refresh = jwtUtil.createToken("refresh", username, role, 24 * 60 * 60 * 1000L); // 24시간

        // 6️⃣ RefreshToken 쿠키 설정
        ResponseCookie cookie = ResponseCookie.from("refresh", refresh)
                .httpOnly(true)
                .secure(false) // HTTPS면 true 권장
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 7️⃣ 프론트로 리다이렉트 (JWT와 유저정보 전달)
        String redirectUrl = "http://404notfoundpage.duckdns.org/oauth2/success"
                + "?accessToken=" + URLEncoder.encode(access, StandardCharsets.UTF_8)
                + "&username=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
                + "&nickname=" + URLEncoder.encode(nickname, StandardCharsets.UTF_8)
                + "&role=" + URLEncoder.encode(role, StandardCharsets.UTF_8);

        response.sendRedirect(redirectUrl);
    }
}
