package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.data.entity.UserInfoEntity;
import com.example.notfound_backend.data.repository.UserAuthRepository;
import com.example.notfound_backend.data.repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserInfoDAO {
    private final UserInfoRepository userInfoRepository;
    private final UserAuthRepository userAuthRepository;

    // 회원가입
    public UserInfoEntity addUserInfo(UserInfoEntity userInfoEntity) {
        return userInfoRepository.save(userInfoEntity);
    }

    // 회원정보 수정
    public UserInfoEntity updateUserInfo(UserInfoEntity userInfoEntity) {
        UserInfoEntity userInfo = userInfoRepository.getByUsername(userInfoEntity.getUsername());
        userInfo.setUsername(userInfoEntity.getUsername());
        userInfo.setNickname(userInfoEntity.getNickname());
        userInfo.setPhone(userInfoEntity.getPhone());
        userInfo.setAddress(userInfoEntity.getAddress());
        return userInfoRepository.save(userInfo);
    }

    // 회원정보 찾기
    public UserInfoEntity getUserInfo(String username) {
        UserAuthEntity userAuthEntity = userAuthRepository.findById(username).orElse(null);
        return userInfoRepository.getByUsername(userAuthEntity);
    }

    // 회원정보리스트 (관리자)
    public List<UserInfoEntity> getAllUserInfo() {
        return userInfoRepository.findAll();
    }

}
