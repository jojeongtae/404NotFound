package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.*;
import com.example.notfound_backend.data.repository.BoardQnaRecommendRepository;
import com.example.notfound_backend.data.repository.BoardQnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardQnaRecommendDAO {
    private final BoardQnaRecommendRepository boardQnaRecommendRepository;
    private final BoardQnaRepository boardQnaRepository;

    public boolean isAlreadyRecommended(BoardQnaEntity board, UserAuthEntity user) {
        return boardQnaRecommendRepository.existsByBoardAndUsernameAndIsActive(board, user, true);
    }

    public void saveRecommend(BoardQnaRecommendEntity entity) {
        boardQnaRecommendRepository.save(entity);
    }

    @Transactional
    public void increaseRecommendCount(Integer boardId) {
        boardQnaRepository.incrementRecommend(boardId);
    }
}
