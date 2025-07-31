package com.example.notfound_backend.data.repository.normalboard.recommend;

import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.normalboard.board.BoardQnaEntity;
import com.example.notfound_backend.data.entity.normalboard.recommend.BoardQnaRecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardQnaRecommendRepository extends JpaRepository<BoardQnaRecommendEntity, Integer> {

    //중복 추천 방지
    boolean existsByBoardAndUsernameAndIsActive(BoardQnaEntity board, UserAuthEntity username, Boolean isActive);

    //추천 취소 기능
    Optional<BoardQnaRecommendEntity> findByBoardAndUsername(BoardQnaEntity board, UserAuthEntity username);

}
