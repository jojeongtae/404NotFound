package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.UserInfoAllDTO;
import com.example.notfound_backend.data.dto.UserInfoDTO;
import com.example.notfound_backend.service.UserAuthService;
import com.example.notfound_backend.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class UserInfoController {
    private final UserInfoService userInfoService;
    private final UserAuthService userAuthService;
    // 유저정보 수정
    @PutMapping(value = "/user/user-info")
    public ResponseEntity<UserInfoDTO> updateUserInfo(@RequestBody UserInfoDTO userInfoDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(userInfoService.updateUserInfo(userInfoDTO));
    }

    // 회원정보 찾기
    @GetMapping(value = "/user/user-info")
    public ResponseEntity<UserInfoAllDTO> getUserInfo(@RequestParam String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userInfoService.getUserInfo(username));
    }
    // 회원등급 찾기
    @GetMapping(value = "/user/user-grade")
    public ResponseEntity<String> getUserGrade(@RequestParam String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userInfoService.getUserGrade(username));
    }

    // 회원정보리스트 (관리자)
    @GetMapping(value = "/admin/users")
    public ResponseEntity<List<UserInfoAllDTO>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userInfoService.getAllUserInfo());
    }

    @GetMapping(value = "/check-username/{username}")
    public ResponseEntity<Boolean> getUserInfoByUsername(@PathVariable String username) { // @PathVariable로 변경
        boolean isExists = userAuthService.findUserByUsername(username);
        if (isExists) {
            return ResponseEntity.ok(false); // 이미 사용 중 → 사용 불가
        } else {
            return ResponseEntity.ok(true);  // 사용 가능
        }
    }
    @GetMapping(value = "/check-nickname/{nickname}")
    public ResponseEntity<Boolean> getUserInfoByNickname(@PathVariable String nickname) {
        boolean isExists = userInfoService.findByNickname(nickname);
        if (isExists) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);
    }


}