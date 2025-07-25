package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.PasswordDTO;
import com.example.notfound_backend.data.dto.UserAuthUpdateDTO;
import com.example.notfound_backend.data.dto.UserJoinDTO;
import com.example.notfound_backend.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class UserAuthController {
    private final UserAuthService userAuthService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<String> addUserAuth(@Valid @RequestBody UserJoinDTO userJoinDTO) {
        userAuthService.addUserAuth(userJoinDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully 회원가입 성공");
    }

    // 비번수정
    @PutMapping("/user/password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UserAuthUpdateDTO userAuthUpdateDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(userAuthService.updatePassword(userAuthUpdateDTO));
    }

    // 회원탈퇴
//    @DeleteMapping("/user/{username}")
//    public ResponseEntity<String> deleteUserAuth(@PathVariable String username, @RequestBody PasswordDTO passwordDTO) {
//        return ResponseEntity.status(HttpStatus.OK).body(userAuthService.deleteUserAuth(username, passwordDTO.getPassword()));
//    }

}