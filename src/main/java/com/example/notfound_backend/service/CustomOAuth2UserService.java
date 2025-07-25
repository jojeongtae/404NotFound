package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dto.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        System.out.println(oAuth2User);
        String resigId=oAuth2UserRequest.getClientRegistration().getRegistrationId();

        OAuth2Response auth2Response=null;

        if(resigId.equals("naver")){
            auth2Response=new NaverOAuth2Response(oAuth2User.getAttributes());
        }else if(resigId.equals("google")){
            auth2Response=new GoogleOAuth2Response(oAuth2User.getAttributes());
        }else{
            return null;
        }

        String username= auth2Response.getProvider()+" "+auth2Response.getProviderId();
        System.out.println("유저 이름 : "+username);

        UserAuthDTO userAuthDTO=new UserAuthDTO();
        userAuthDTO.setUsername(username);
        userAuthDTO.setRole("ROLE_ADMIN");

        return new CustomOAuth2User(userAuthDTO);
    }

}
