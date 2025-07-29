package com.example.notfound_backend.data.repository.normalboard.recommend;

import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.normalboard.board.BoardFoodEntity;
import com.example.notfound_backend.data.entity.normalboard.recommend.BoardFoodRecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardFoodRecommendRepository extends JpaRepository<BoardFoodRecommendEntity, Integer> {

    //중복 추천 방지
    boolean existsByBoardAndUsernameAndIsActive(BoardFoodEntity board, UserAuthEntity username, Boolean isActive);

    //추천 취소 기능
    Optional<BoardFoodRecommendEntity> findByBoardAndUsername(BoardFoodEntity board, UserAuthEntity username);

}
