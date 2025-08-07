package com.example.notfound_backend.data.repository.normalboard.recommend;

import com.example.notfound_backend.data.entity.normalboard.board.BoardUsedEntity;
import com.example.notfound_backend.data.entity.normalboard.recommend.BoardUsedRecommendEntity;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardUsedRecommendRepository extends JpaRepository<BoardUsedRecommendEntity, Integer> {

    //중복 추천 방지
    boolean existsByBoardAndUsernameAndIsActive(BoardUsedEntity board, UserAuthEntity username, Boolean isActive);

    //추천 취소 기능
    Optional<BoardUsedRecommendEntity> findByBoardAndUsername(BoardUsedEntity board, UserAuthEntity username);

}
