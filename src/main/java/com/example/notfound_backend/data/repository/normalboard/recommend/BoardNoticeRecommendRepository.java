package com.example.notfound_backend.data.repository.normalboard.recommend;

import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.normalboard.board.BoardNoticeEntity;
import com.example.notfound_backend.data.entity.normalboard.recommend.BoardNoticeRecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardNoticeRecommendRepository extends JpaRepository<BoardNoticeRecommendEntity, Integer> {

    //중복 추천 방지
    boolean existsByBoardAndUsernameAndIsActive(BoardNoticeEntity board, UserAuthEntity username, Boolean isActive);

    //추천 취소 기능
    Optional<BoardNoticeRecommendEntity> findByBoardAndUsername(BoardNoticeEntity board, UserAuthEntity username);

}
