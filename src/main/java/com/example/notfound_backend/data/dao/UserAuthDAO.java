package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.data.repository.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserAuthDAO {
    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원정보
    public UserAuthEntity findByUsername(String username) {
        return userAuthRepository.findById(username).orElse(null);
    }

    // 회원가입
    public UserAuthEntity addUserAuth(String username, String password, String role) {
        UserAuthEntity userAuthEntity = UserAuthEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(role)
                .joindate(LocalDateTime.now())
                .build();
        return userAuthRepository.save(userAuthEntity);
    }


}
