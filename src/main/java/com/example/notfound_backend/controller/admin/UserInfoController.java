package com.example.notfound_backend.controller.admin;

import com.example.notfound_backend.data.dao.admin.UserInfoDAO;
import com.example.notfound_backend.data.dto.admin.UserInfoAllDTO;
import com.example.notfound_backend.data.dto.admin.UserInfoDTO;
import com.example.notfound_backend.data.entity.enumlist.UserStatus;
import com.example.notfound_backend.service.login.UserAuthService;
import com.example.notfound_backend.service.admin.UserInfoService;
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
    private final UserInfoDAO userInfoDAO;
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
    public ResponseEntity<String> getUserGrade(@RequestParam(required = false) String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userInfoService.getUserGrade(username));
    }

    // 회원정보리스트 (관리자)
    @GetMapping(value = "/admin/users")
    public ResponseEntity<List<UserInfoAllDTO>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userInfoService.getAllUserInfo());
    }
    // 회원 상태변경 (관리자)
    @PatchMapping(value = "/admin/user-status/{username}")
    public ResponseEntity<UserInfoAllDTO> updateUserStatus(@PathVariable String username, @RequestParam UserStatus status) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(userInfoService.updateStatus(username, status));
    }

    @GetMapping(value = "/check-username/{username}")
    public ResponseEntity<Boolean> getUserInfoByUsername(@PathVariable String username) { // @PathVariable로 변경
        boolean isExists = userAuthService.findUserByUsername(username);
        return ResponseEntity.ok(!isExists);
    }
    @GetMapping(value = "/check-nickname/{nickname}")
    public ResponseEntity<Boolean> getUserInfoByNickname(@PathVariable String nickname) {
        boolean isExists = userInfoService.findByNickname(nickname);
        return ResponseEntity.ok(!isExists);
    }
    @GetMapping(value = "/user/nickname/{nickname}")
    public ResponseEntity<UserInfoDTO> findUserInfoByNickname(@PathVariable String nickname) {
        UserInfoDTO userInfo = userInfoService.findUserInfoByNickname(nickname);
        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    }

}