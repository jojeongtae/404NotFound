package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardUsedDAO;
import com.example.notfound_backend.data.dao.BoardUsedRecommendDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.entity.BoardUsedEntity;
import com.example.notfound_backend.data.entity.BoardUsedRecommendEntity;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BoardUsedRecommendService {
    private final BoardUsedRecommendDAO  boardUsedRecommendDAO;
    private final BoardUsedDAO boardUsedDAO;
    private final UserAuthDAO userAuthDAO;

    public void recommend(Integer boardId, String username) {

        BoardUsedEntity board = boardUsedDAO.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        UserAuthEntity user = userAuthDAO.findByUsername(username);

        if (boardUsedRecommendDAO.isAlreadyRecommended(board, user)) {
            throw new IllegalStateException("이미 추천했습니다.");
        }

        BoardUsedRecommendEntity entity = new BoardUsedRecommendEntity();
        entity.setBoard(board);
        entity.setUsername(user);
        entity.setIsActive(true);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        boardUsedRecommendDAO.saveRecommend(entity);
        boardUsedRecommendDAO.increaseRecommendCount(board.getId());
    }

}
