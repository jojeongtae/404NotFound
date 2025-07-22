package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.*;
import com.example.notfound_backend.data.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BoardInfoRecommendService {

    private final BoardInfoRecommendDAO boardInfoRecommendDAO;
    private final BoardInfoDAO boardInfoDAO;
    private final UserAuthDAO userAuthDAO;

    public void recommend(Integer boardId, String username) {

        BoardInfoEntity board = boardInfoDAO.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        UserAuthEntity user = userAuthDAO.findByUsername(username);

        if (boardInfoRecommendDAO.isAlreadyRecommended(board, user)) {
            throw new IllegalStateException("이미 추천했습니다.");
        }

        BoardInfoRecommendEntity entity = new BoardInfoRecommendEntity();
        entity.setBoard(board);
        entity.setUsername(user);
        entity.setIsActive(true);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        boardInfoRecommendDAO.saveRecommend(entity);
        boardInfoRecommendDAO.increaseRecommendCount(board.getId());
    }

}
