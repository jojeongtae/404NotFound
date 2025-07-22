package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.UserAuthUpdateDTO;
import com.example.notfound_backend.data.dto.UserJoinDTO;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.data.entity.UserInfoEntity;
import com.example.notfound_backend.data.entity.UserStatus;
import com.example.notfound_backend.exception.DuplicateIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthService implements UserDetailsService {
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;
    private final PasswordEncoder passwordEncoder;

    // 인증처리
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthEntity userAuthEntity = this.userAuthDAO.findByUsername(username);
        if (userAuthEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(userAuthEntity.getRole()));
        return new User(userAuthEntity.getUsername(), userAuthEntity.getPassword(), grantedAuthorities);
    }

    // 회원가입
    public String addUserAuth(UserJoinDTO userJoinDTO) {
        if (userJoinDTO.getUsername() == null || userJoinDTO.getPassword() == null) {
            throw new IllegalArgumentException("username과 password는 필수입니다.");
        }
        if (userAuthDAO.findByUsername(userJoinDTO.getUsername()) != null) {
            throw new DuplicateIdException("동일ID가 존재합니다.");
        }
        userAuthDAO.addUserAuth(userJoinDTO.getUsername(), userJoinDTO.getPassword(), "ROLE_USER");

        UserAuthEntity userAuthEntity = UserAuthEntity.builder()
                .username(userJoinDTO.getUsername())
                .build();

        UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                .username(userAuthEntity)
                .nickname(userJoinDTO.getNickname())
                .phone(userJoinDTO.getPhone())
                .address(userJoinDTO.getAddress())
                .point(0)
                .warning(0)
                .status(UserStatus.ACTIVE)
                .build();
        userInfoDAO.addUserInfo(userInfoEntity);
        return userJoinDTO.getUsername() + " 회원가입 성공";
    }

    // 비번수정
    @Transactional
    public String updatePassword(UserAuthUpdateDTO userAuthUpdateDTO) {
        UserAuthEntity userAuthEntity = userAuthDAO.findByUsername(userAuthUpdateDTO.getUsername());
        if (userAuthEntity == null) {
            throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
        }
        if (!passwordEncoder.matches(userAuthUpdateDTO.getOldPassword(), userAuthEntity.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호 불일치");
        } else if (userAuthUpdateDTO.getOldPassword().equals(userAuthUpdateDTO.getNewPassword())) {
            throw new IllegalArgumentException("기존 비밀번호와 새 비밀번호가 같습니다.");
        } else {
            int result = userAuthDAO.updatePassword(userAuthUpdateDTO.getUsername(), userAuthUpdateDTO.getNewPassword());
            return (result == 1) ? "비밀번호 수정 성공" : "비밀번호 수정 실패";
        }
    }

    public boolean findUserByUsername(String username) {
        UserAuthEntity userAuthEntity = this.userAuthDAO.findByUsername(username);
        if (userAuthEntity == null) {
            return true;
        }
        return false;
    }


}
