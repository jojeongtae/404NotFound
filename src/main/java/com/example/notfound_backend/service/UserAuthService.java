package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.UserAuthDTO;
import com.example.notfound_backend.data.dto.UserJoinDTO;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.data.entity.UserInfoEntity;
import com.example.notfound_backend.exception.DuplicateIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthService implements UserDetailsService {
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;

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
                //.password(userJoinDTO.getPassword())
                .build();

        UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                .username(userAuthEntity)
                .nickname(userJoinDTO.getNickname())
                .phone(userJoinDTO.getPhone())
                .address(userJoinDTO.getAddress())
                .point(0)
                .warning(0)
                .build();
        userInfoDAO.addUserInfo(userInfoEntity);
        return userJoinDTO.getUsername() + " 회원가입 성공";
    }




}
