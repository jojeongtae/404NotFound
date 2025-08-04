package com.example.notfound_backend.controller.login;

import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
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

    // Step 1: 리다이렉트 to Kakao OAuth2 인증 페이지
    @GetMapping("/kakao")
    public ResponseEntity<?> redirectToKakaoLogin() {
        String authorizationUrl = UriComponentsBuilder.fromHttpUrl(KAKAO_AUTH_URL)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .toUriString();

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, authorizationUrl)
                .build();
    }

    // Step 2: 카카오 인증 후 전달된 code 처리
    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<Map<String, String>> handleKakaoCallback(
            @RequestParam String code,
            @RequestHeader(value = "androidApp", required = false) String app) {

        boolean isApp = app != null && app.equalsIgnoreCase("AndroidApp");

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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Map<String, Object> body = userInfoResponse.getBody();
        Long kakaoId = ((Number) body.get("id")).longValue();
        Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");
        String role = "ROLE_USER";
        String username = (email != null) ? email : "kakao_" + kakaoId;

        // 사용자 정보 저장 또는 업데이트

        // 3️⃣ JWT 생성
        String access = jwtUtil.createToken("access", username, role, 60*10*1000L); // --추가된부분--
        String refresh = jwtUtil.createToken("refresh", username, role, 24*60*60*1000L); // --추가된부분--

        // 4️⃣ 쿠키 세팅 (RefreshToken)
        ResponseCookie cookie = ResponseCookie.from("refresh", refresh) // --추가된부분--
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(24*60*60)
                .sameSite("Lax")
                .build();

        // 5️⃣ JSON Body 반환
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("username", username);       // --추가된부분--
        responseBody.put("role", role);               // --추가된부분--
        responseBody.put("nickname", nickname);       // --추가된부분--
        responseBody.put("accessToken", access);      // --수정된부분-- (카카오 토큰 → 내 JWT)

        return ResponseEntity.ok() // --추가된부분--
                .header(HttpHeaders.SET_COOKIE, cookie.toString()) // --추가된부분--
                .body(responseBody); // --추가된부분--
    }

}