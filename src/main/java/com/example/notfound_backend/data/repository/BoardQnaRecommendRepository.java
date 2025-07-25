package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardQnaRecommendRepository extends JpaRepository<BoardQnaRecommendEntity, Integer> {

    //중복 추천 방지
    boolean existsByBoardAndUsernameAndIsActive(BoardQnaEntity board, UserAuthEntity username, Boolean isActive);

    //추천 취소 기능
    Optional<BoardQnaRecommendEntity> findByBoardAndUsername(BoardQnaEntity board, UserAuthEntity username);

}
