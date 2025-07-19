package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.data.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Integer> {

    // 회원정보 찾기
    UserInfoEntity getByUsername(UserAuthEntity username);

     UserInfoEntity findByNickname(String nickname);

}