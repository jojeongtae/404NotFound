package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuthEntity, String> {

    // 비번수정


}
