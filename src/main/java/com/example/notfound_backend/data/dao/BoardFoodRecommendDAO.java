package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.*;
import com.example.notfound_backend.data.repository.BoardFoodRecommendRepository;
import com.example.notfound_backend.data.repository.BoardFoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardFoodRecommendDAO {
    private final BoardFoodRecommendRepository boardFoodRecommendRepository;
    private final BoardFoodRepository boardFoodRepository;

    public boolean isAlreadyRecommended(BoardFoodEntity board, UserAuthEntity user) {
        return boardFoodRecommendRepository.existsByBoardAndUsernameAndIsActive(board, user, true);
    }

    public void saveRecommend(BoardFoodRecommendEntity entity) {
        boardFoodRecommendRepository.save(entity);
    }

    @Transactional
    public void increaseRecommendCount(Integer boardId) {
        boardFoodRepository.incrementRecommend(boardId);
    }
}
