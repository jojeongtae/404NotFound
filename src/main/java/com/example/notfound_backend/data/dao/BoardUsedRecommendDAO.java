package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.BoardUsedEntity;
import com.example.notfound_backend.data.entity.BoardUsedRecommendEntity;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.data.repository.BoardUsedRecommendRepository;
import com.example.notfound_backend.data.repository.BoardUsedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardUsedRecommendDAO {
    private final BoardUsedRepository boardUsedRepository;
    private final BoardUsedRecommendRepository boardUsedRecommendRepository;

    public boolean isAlreadyRecommended(BoardUsedEntity board, UserAuthEntity user) {
        return boardUsedRecommendRepository.existsByBoardAndUsernameAndIsActive(board, user, true);
    }

    public void saveRecommend(BoardUsedRecommendEntity entity) {
        boardUsedRecommendRepository.save(entity);
    }

    @Transactional
    public void increaseRecommendCount(Integer boardId) {
        boardUsedRepository.incrementRecommend(boardId);
    }
}
