package com.example.notfound_backend.controller;


import com.example.notfound_backend.data.dto.UserAuthDTO;
import com.example.notfound_backend.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class UserAuthController {
    private final UserAuthService userAuthService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<String> addUserAuth(@Valid @RequestBody UserAuthDTO userAuthDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { // 유효성 검사 실패시
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(message);
        }
        userAuthService.addUserAuth(userAuthDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully 회원가입 성공");
    }

}
