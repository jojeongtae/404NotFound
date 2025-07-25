package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.*;
import com.example.notfound_backend.data.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BoardQnaRecommendService {

    private final BoardQnaRecommendDAO boardQnaRecommendDAO;
    private final BoardQnaDAO boardQnaDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoService userInfoService;

    public void recommend(Integer boardId, String username) {
        userInfoService.userStatusValidator(username);

        BoardQnaEntity board = boardQnaDAO.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        UserAuthEntity user = userAuthDAO.findByUsername(username);

        if (boardQnaRecommendDAO.isAlreadyRecommended(board, user)) {
            throw new IllegalStateException("이미 추천했습니다.");
        }

        BoardQnaRecommendEntity entity = new BoardQnaRecommendEntity();
        entity.setBoard(board);
        entity.setUsername(user);
        entity.setIsActive(true);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        boardQnaRecommendDAO.saveRecommend(entity);
        boardQnaRecommendDAO.increaseRecommendCount(board.getId());
    }

}
