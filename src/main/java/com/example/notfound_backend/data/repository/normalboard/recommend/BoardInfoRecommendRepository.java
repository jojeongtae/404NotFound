package com.example.notfound_backend.data.repository.normalboard.recommend;

import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.normalboard.board.BoardInfoEntity;
import com.example.notfound_backend.data.entity.normalboard.recommend.BoardInfoRecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardInfoRecommendRepository extends JpaRepository<BoardInfoRecommendEntity, Integer> {

    //중복 추천 방지
    boolean existsByBoardAndUsernameAndIsActive(BoardInfoEntity board, UserAuthEntity username, Boolean isActive);

    //추천 취소 기능
    Optional<BoardInfoRecommendEntity> findByBoardAndUsername(BoardInfoEntity board, UserAuthEntity username);

}
