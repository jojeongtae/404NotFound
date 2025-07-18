package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.UserInfoAllDTO;
import com.example.notfound_backend.data.dto.UserInfoDTO;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.data.entity.UserInfoEntity;
import com.example.notfound_backend.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserInfoDAO userInfoDAO;
    private final UserAuthDAO userAuthDAO;

    // 회원정보 수정
    @Transactional // 실행중 예외발생시 자동으로 롤백
    public int updateUserInfo(UserInfoDTO userInfoDTO) {
        if (userAuthDAO.findByUsername(userInfoDTO.getUsername()) == null) {
            throw new UserNotFoundException("존재하지않는 username 입니다.");
        }
        return userInfoDAO.updateUserInfo(userInfoDTO.getUsername(), userInfoDTO.getNickname(), userInfoDTO.getPhone(), userInfoDTO.getAddress());
    }

    // 회원정보 찾기
    public UserInfoAllDTO getUserInfo(String username) {
        UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(username);
        return UserInfoAllDTO.builder()
                .username(userInfoEntity.getUsername().getUsername())
                .nickname(userInfoEntity.getNickname())
                .phone(userInfoEntity.getPhone())
                .address(userInfoEntity.getAddress())
                .point(userInfoEntity.getPoint())
                .warning(userInfoEntity.getWarning())
                .build();
    }
    public boolean findByNickname(String nickname) {
        UserInfoEntity userInfoEntity = userInfoDAO.findByNickname(nickname);
        if (userInfoEntity == null) {
            return true;
        }
        return false;
    }




}
