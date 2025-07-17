package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.UserInfoAllDTO;
import com.example.notfound_backend.data.dto.UserInfoDTO;
import com.example.notfound_backend.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
public class UserInfoController {
    private final UserInfoService userInfoService;

    // 유저정보 수정
    @PutMapping(value = "/user-info")
    public ResponseEntity<UserInfoDTO> updateUserInfo(@RequestBody UserInfoDTO userInfoDTO) {
        userInfoService.updateUserInfo(userInfoDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userInfoDTO);
    }

    // 회원정보 찾기
    @GetMapping(value = "/user-info")
    public ResponseEntity<UserInfoAllDTO> getUserInfo(@RequestParam String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userInfoService.getUserInfo(username));
    }


}
