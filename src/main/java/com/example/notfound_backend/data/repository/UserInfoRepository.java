package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.data.entity.UserInfoEntity;
import com.example.notfound_backend.data.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Integer> {

    // 회원정보 찾기
    UserInfoEntity getByUsername(UserAuthEntity username);

    UserInfoEntity findByNickname(String nickname);

    @Modifying
    @Query("update UserInfoEntity u set u.point = u.point + :point where u.username.username = :username")
    Integer updatePoint(@Param("username") String username, @Param("point") Integer point);

    @Modifying
    @Query("update UserInfoEntity u set u.warning = u.warning + :warning where u.username.username = :username")
    Integer updateWarning(@Param("username") String username, @Param("warning") Integer warning);

    @Modifying
    @Query("update UserInfoEntity u set u.status = :status where u.username.username = :username")
    Integer updateStatus(@Param("username") String username, @Param("status") UserStatus status);

}