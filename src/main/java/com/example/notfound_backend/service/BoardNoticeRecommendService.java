package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.*;
import com.example.notfound_backend.data.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BoardNoticeRecommendService {
    private final BoardNoticeRecommendDAO boardNoticeRecommendDAO;
    private final BoardNoticeDAO boardNoticeDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoService userInfoService;

    public void recommend(Integer boardId, String username) {
        userInfoService.userStatusValidator(username);

        BoardNoticeEntity board = boardNoticeDAO.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        UserAuthEntity user = userAuthDAO.findByUsername(username);

        if (boardNoticeRecommendDAO.isAlreadyRecommended(board, user)) {
            throw new IllegalStateException("이미 추천했습니다.");
        }

        BoardNoticeRecommendEntity entity = new BoardNoticeRecommendEntity();
        entity.setBoard(board);
        entity.setUsername(user);
        entity.setIsActive(true);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        boardNoticeRecommendDAO.saveRecommend(entity);
        boardNoticeRecommendDAO.increaseRecommendCount(board.getId());
    }

}
