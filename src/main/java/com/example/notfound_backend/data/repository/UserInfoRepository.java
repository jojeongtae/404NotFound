package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.data.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Integer> {

    // 유저정보 수정
    @Modifying
    @Query("UPDATE UserInfoEntity u SET u.nickname = :nickname, u.phone = :phone, u.address = :address WHERE u.username.username = :username")
    int updateUserInfo(@Param("username") String username, @Param("nickname") String nickname, @Param("phone") String phone, @Param("address") String address);

    // 회원정보 찾기
    UserInfoEntity getByUsername(UserAuthEntity username);

}
