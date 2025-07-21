package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.data.repository.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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

    // 비번수정
    public int updatePassword(String username, String password) {
        return userAuthRepository.updatePassword(username, passwordEncoder.encode(password));
    }

    // 회원탈퇴


    public String getRole(String username) {
        Optional<UserAuthEntity> userAuth = userAuthRepository.findById(username);
        return userAuth.map(UserAuthEntity::getRole).orElse(null);
    }

    public boolean isAdmin(String username) {
        Optional<UserAuthEntity> userAuth = userAuthRepository.findById(username);
        return userAuth.isPresent() && userAuth.get().getRole().equals("ROLE_ADMIN");
    }


}
