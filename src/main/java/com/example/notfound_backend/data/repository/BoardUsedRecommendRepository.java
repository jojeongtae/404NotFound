package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.BoardUsedEntity;
import com.example.notfound_backend.data.entity.BoardUsedRecommendEntity;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardUsedRecommendRepository extends JpaRepository<BoardUsedRecommendEntity, Integer> {

    //중복 추천 방지
    boolean existsByBoardAndUsernameAndIsActive(BoardUsedEntity board, UserAuthEntity username, Boolean isActive);

    //추천 취소 기능
    Optional<BoardUsedRecommendEntity> findByBoardAndUsername(BoardUsedEntity board, UserAuthEntity username);


}
