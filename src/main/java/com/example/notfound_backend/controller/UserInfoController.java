package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.UserInfoAllDTO;
import com.example.notfound_backend.data.dto.UserInfoDTO;
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

    // 회원정보리스트 (관리자)
    @GetMapping(value = "/admin/users")
    public ResponseEntity<List<UserInfoAllDTO>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userInfoService.getAllUserInfo());
    }


}
