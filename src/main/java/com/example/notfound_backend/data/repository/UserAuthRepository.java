package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAuthRepository extends JpaRepository<UserAuthEntity, String> {

    // 비번수정
    @Modifying
    @Query("UPDATE UserAuthEntity u SET u.password=:password WHERE u.username=:username")
    int updatePassword(@Param("username") String username, @Param("password") String password);

}
