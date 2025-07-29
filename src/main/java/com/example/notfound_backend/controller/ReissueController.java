package com.example.notfound_backend.controller;

import com.example.notfound_backend.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class ReissueController {
    private final JwtUtil jwtUtil;

    // refresh토큰으로 access토큰 재발행
    @PostMapping(value = "/reissue")
    public ResponseEntity<String> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refreshToken = cookie.getValue();
                break;
            }
        }
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is empty. 토큰없음");
        }
        try {

        } catch (ExpiredJwtException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is expired. 만료된 토큰");
        }

        String category = this.jwtUtil.getCategory(refreshToken);
        if(!category.equals("refresh")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is invalid. 유효하지 않은 토큰");
        }

        String username = jwtUtil.getUserName(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        String accessToken = this.jwtUtil.createToken("access", username, role, 10 * 1000L); // 10초
        response.addHeader("Authorization", "Bearer " + accessToken);
        return ResponseEntity.status(HttpStatus.OK).body("Reissued access token successfully. 토큰 재발행 성공");
    }

    // 로그아웃(쿠키만료)
    @DeleteMapping(value = "/reissue")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.OK).body("Refresh token is expired. 로그아웃 성공");
    }

}
