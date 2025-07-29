package com.example.notfound_backend.data.repository.normalboard.recommend;

import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.normalboard.board.BoardFreeEntity;
import com.example.notfound_backend.data.entity.normalboard.recommend.BoardFreeRecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardFreeRecommendRepository extends JpaRepository<BoardFreeRecommendEntity, Integer> {

    //중복 추천 방지
    boolean existsByBoardAndUsernameAndIsActive(BoardFreeEntity board, UserAuthEntity username, Boolean isActive);

    //추천 취소 기능
    Optional<BoardFreeRecommendEntity> findByBoardAndUsername(BoardFreeEntity board, UserAuthEntity username);

}
