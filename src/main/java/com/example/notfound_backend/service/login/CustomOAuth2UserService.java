package com.example.notfound_backend.service.login;


import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.repository.login.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserAuthRepository userAuthRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // kakao, naver, github
        Map<String, Object> attributes = oauth2User.getAttributes();

        // ✅ 소셜 로그인 제공자별로 id 추출
        String id;
        if ("kakao".equals(registrationId)) {
            id = attributes.get("id").toString();
        } else if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            id = response.get("id").toString();
        } else { // github 등
            id = attributes.get("id").toString();
        }

        String username = id;

        // ✅ DB 조회 or 신규 생성
        UserAuthEntity user = userAuthRepository.findByUsername(username);

        if (user == null) {
            user = createNewSocialUser(username, registrationId);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole())),
                attributes,
                "id" // 기본 키로 쓸 attribute
        );
    }

    private UserAuthEntity createNewSocialUser(String username, String provider) {
        UserAuthEntity newUser = new UserAuthEntity();
        newUser.setUsername(username);
        newUser.setPassword(""); // 소셜로그인 사용자는 비번 없음
        newUser.setRole("ROLE_USER");
        return userAuthRepository.save(newUser);
    }
}
