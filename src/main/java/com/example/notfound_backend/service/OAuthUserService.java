package com.example.notfound_backend.service;

import com.example.notfound_backend.data.entity.admin.UserInfoEntity;
import com.example.notfound_backend.data.entity.enumlist.UserStatus;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.repository.admin.UserInfoRepository;
import com.example.notfound_backend.data.repository.login.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OAuthUserService {

    private final UserAuthRepository userAuthRepository;
    private final UserInfoRepository userInfoRepository;

    @Transactional
    public UserInfoEntity processOAuthUser(String username, String role, String nickname, String phone, String address) {
        // 1️⃣ UserAuthEntity 처리 (기존/신규)
        UserAuthEntity user = userAuthRepository.findByUsername(username);

        if (user == null) {
            // 신규 유저일 경우, 새로 생성
            user = new UserAuthEntity();
            user.setUsername(username);
            user.setJoindate(LocalDateTime.now());
        }

        // 역할, 비밀번호 등은 항상 최신 정보로 업데이트 후 저장
        user.setRole(role);
        user.setPassword(""); // 소셜 로그인은 비번 없음
        final UserAuthEntity savedUser = userAuthRepository.save(user);

        // 2️⃣ UserInfoEntity 처리 (기존/신규) - final 변수를 사용하여 람다 오류 해결
        UserInfoEntity info = userInfoRepository.findByUsername(savedUser)
                .orElseGet(() -> UserInfoEntity.builder()
                        .username(savedUser)             // FK 설정
                        .status(UserStatus.ACTIVE)       // 기본 상태
                        .warning(0)                      // 기본 경고 수
                        .point(0)
                        .build());

        // 사용자 정보 업데이트
        info.setNickname(nickname);
        info.setPhone(phone);
        info.setAddress(address);
        info.setStatus(UserStatus.ACTIVE);
        if (info.getWarning() == null) info.setWarning(0);

        return userInfoRepository.save(info);
    }
}
