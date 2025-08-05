package com.example.notfound_backend.service.admin;

import com.example.notfound_backend.data.dao.login.UserAuthDAO;
import com.example.notfound_backend.data.dao.admin.UserInfoDAO;
import com.example.notfound_backend.data.dto.admin.UserInfoAllDTO;
import com.example.notfound_backend.data.dto.admin.UserInfoDTO;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.admin.UserInfoEntity;
import com.example.notfound_backend.data.entity.enumlist.UserStatus;
import com.example.notfound_backend.exception.UserNotFoundException;
import com.example.notfound_backend.exception.UserSuspendedException;
import com.example.notfound_backend.service.login.UserAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserInfoDAO userInfoDAO;
    private final UserAuthDAO userAuthDAO;
    private final ConversionService conversionService;
    private final UserAuthService userAuthService;

    // 회원정보 수정
    @Transactional // 실행중 예외발생시 자동으로 롤백
    public UserInfoDTO updateUserInfo(UserInfoDTO userInfoDTO) {
        UserAuthEntity userAuth = userAuthDAO.findByUsername(userInfoDTO.getUsername());
        if (userAuth == null) {
            throw new UserNotFoundException("존재하지않는 username 입니다.");
        }
        UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                .username(userAuth)
                .nickname(userInfoDTO.getNickname())
                .phone(userInfoDTO.getPhone())
                .address(userInfoDTO.getAddress())
                .build();
        UserInfoEntity updatedUserInfo = userInfoDAO.updateUserInfo(userInfoEntity);
        return UserInfoDTO.builder()
                .username(updatedUserInfo.getUsername().getUsername())
                .nickname(updatedUserInfo.getNickname())
                .phone(updatedUserInfo.getPhone())
                .address(updatedUserInfo.getAddress())
                .grade(getUserGrade(updatedUserInfo.getUsername().getUsername()))
                .build();
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
                .status(userInfoEntity.getStatus())
                .grade(getUserGrade(userInfoEntity.getUsername().getUsername()))
                .build();
    }
    public boolean findByNickname(String nickname) {
        UserInfoEntity userInfoEntity = userInfoDAO.findByNickname(nickname);
        return userInfoEntity != null;
    }
    // 닉네입으로 유저정보 찾기
    public UserInfoDTO findUserInfoByNickname(String nickname) {
        UserInfoEntity userInfoEntity = userInfoDAO.findByNickname(nickname);
        if (userInfoEntity == null) {
            throw new UserNotFoundException("일치하는 닉네임 찾을 수 없음.");
        }
        return UserInfoDTO.builder()
                .username(userInfoEntity.getUsername().getUsername())
                .nickname(userInfoEntity.getNickname())
                .phone(userInfoEntity.getPhone())
                .address(userInfoEntity.getAddress())
                .grade(getUserGrade(userInfoEntity.getUsername().getUsername()))
                .build();
    }

    // 회원등급
    public String getUserGrade(String username) {
        if (username == null || username.isBlank()) {
            return "401 Unauthorized (손님)";
        }
        UserInfoEntity userInfo = userInfoDAO.getUserInfo(username);
        UserAuthEntity userAuth = userAuthDAO.findByUsername(username);
        if (userInfo == null || userAuth == null) {
            return "401 Unauthorized (손님)";
        }
        Integer point = userInfo.getPoint();
        String role = userAuth.getRole();
        if (role.equals("ROLE_ADMIN")) {
            return "500 Internal Server Error (운영진)";
        }
        if (point < 100) {
            return "404 Not Found (신규)";
         } else if (point < 200) {
            return "200 OK (일반 회원)";
        } else if (point < 300) {
            return "202 Accepted (활동 회원)";
        } else {
            return "403 Forbidden (우수 회원)";
        }
    }

    // 회원정보리스트 (관리자)
    public List<UserInfoAllDTO> getAllUserInfo() {
        List<UserInfoEntity> userInfoEntityList = userInfoDAO.getAllUserInfo();
        List<UserInfoAllDTO> userInfoDTOList = new ArrayList<>();
        for (UserInfoEntity userInfoEntity : userInfoEntityList) {
            UserInfoAllDTO userInfoAllDTO = UserInfoAllDTO.builder()
                    .username(userInfoEntity.getUsername().getUsername())
                    .nickname(userInfoEntity.getNickname())
                    .phone(userInfoEntity.getPhone())
                    .address(userInfoEntity.getAddress())
                    .point(userInfoEntity.getPoint())
                    .warning(userInfoEntity.getWarning())
                    .status(userInfoEntity.getStatus())
                    .grade(getUserGrade(userInfoEntity.getUsername().getUsername()))
                    .build();
            userInfoDTOList.add(userInfoAllDTO);
        }
        return userInfoDTOList;
    }

    // 회원 상태변경 (관리자)
    @Transactional
    public UserInfoAllDTO updateStatus(String username, UserStatus status) throws Exception {
        Integer result = userInfoDAO.updateStatus(username, status);
        if (result == 1) {
            return this.getUserInfo(username);
        } else if (result == 0) {
            throw new UserNotFoundException("일치하는 유저 없음");
        } else {
            throw new Exception("상태변경 실패");
        }
    }

    // 회원 상태 체크
    public void userStatusValidator(String username) {
        UserStatus userStatus = userInfoDAO.getUserInfo(username).getStatus();
        if (userStatus == UserStatus.SUSPENDED) {
            throw new UserSuspendedException("활동 정지된 사용자 입니다.");
        } else if (userStatus == UserStatus.BANNED) {
            throw new UserSuspendedException("영구정지 사용자 입니다.");
        } else if (userStatus == UserStatus.INACTIVE) {
            throw new UserSuspendedException("비활성화된 계정 입니다.");
        }
    }

    public List<UserInfoDTO> getTop5UserGradesByPoint() {
        return userInfoDAO.getAllUserInfo().stream()
                .sorted(Comparator.comparingInt(UserInfoEntity::getPoint).reversed())  // 포인트 내림차순
                .limit(5)
                .map(user
                        -> UserInfoDTO.builder()
                        .nickname(user.getNickname())
                        .grade(getUserGrade(user.getUsername().getUsername()))
                        .build())
                .collect(Collectors.toList());
    }


}
