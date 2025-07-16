package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dto.UserAuthDTO;
import com.example.notfound_backend.data.entity.UserAuthEntity;
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
    public String addUserAuth(UserAuthDTO userAuthDTO) {
        if (userAuthDTO.getUsername() == null || userAuthDTO.getPassword() == null) {
            throw new IllegalArgumentException("username과 password는 필수입니다.");
        }
        if (userAuthDAO.findByUsername(userAuthDTO.getUsername()) != null) {
            throw new DuplicateIdException("동일ID가 존재합니다.");
        }
        userAuthDAO.addUserAuth(userAuthDTO.getUsername(), userAuthDTO.getPassword(), "ROLE_USER");
        return userAuthDTO.getUsername() + " 회원가입 성공";
    }




}
