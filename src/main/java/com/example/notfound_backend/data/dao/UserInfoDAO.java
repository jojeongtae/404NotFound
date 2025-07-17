package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.data.entity.UserInfoEntity;
import com.example.notfound_backend.data.repository.UserAuthRepository;
import com.example.notfound_backend.data.repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
//    @Transactional // 실행중 예외발생시 자동으로 롤백
    public int updateUserInfo(String username, String nickname, String phone, String address) {
        return userInfoRepository.updateUserInfo(username, nickname, phone, address);
    }

    // 회원정보 찾기
    public UserInfoEntity getUserInfo(String username) {
        UserAuthEntity userAuthEntity = userAuthRepository.findById(username).orElse(null);
        return userInfoRepository.getByUsername(userAuthEntity);
    }

}
