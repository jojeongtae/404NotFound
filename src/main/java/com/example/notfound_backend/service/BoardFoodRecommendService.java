package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.*;
import com.example.notfound_backend.data.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BoardFoodRecommendService {

    private final BoardFoodRecommendDAO boardFoodRecommendDAO;
    private final BoardFoodDAO boardFoodDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoService userInfoService;

    public void recommend(Integer boardId, String username) {
        userInfoService.userStatusValidator(username);

        BoardFoodEntity board = boardFoodDAO.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        UserAuthEntity user = userAuthDAO.findByUsername(username);

        if (boardFoodRecommendDAO.isAlreadyRecommended(board, user)) {
            throw new IllegalStateException("이미 추천했습니다.");
        }

        BoardFoodRecommendEntity entity = new BoardFoodRecommendEntity();
        entity.setBoard(board);
        entity.setUsername(user);
        entity.setIsActive(true);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        boardFoodRecommendDAO.saveRecommend(entity);
        boardFoodRecommendDAO.increaseRecommendCount(board.getId());
    }

}
