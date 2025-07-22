package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.*;
import com.example.notfound_backend.data.repository.BoardFreeRecommendRepository;
import com.example.notfound_backend.data.repository.BoardFreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardFreeRecommendDAO {
    private final BoardFreeRecommendRepository boardFreeRecommendRepository;
    private final BoardFreeRepository boardFreeRepository;

    public boolean isAlreadyRecommended(BoardFreeEntity board, UserAuthEntity user) {
        return boardFreeRecommendRepository.existsByBoardAndUsernameAndIsActive(board, user, true);
    }

    public void saveRecommend(BoardFreeRecommendEntity entity) {
        boardFreeRecommendRepository.save(entity);
    }

    @Transactional
    public void increaseRecommendCount(Integer boardId) {
        boardFreeRepository.incrementRecommend(boardId);
    }
}
