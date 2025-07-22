package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.*;
import com.example.notfound_backend.data.repository.BoardInfoRecommendRepository;
import com.example.notfound_backend.data.repository.BoardInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardInfoRecommendDAO {
    private final BoardInfoRecommendRepository boardInfoRecommendRepository;
    private final BoardInfoRepository boardInfoRepository;

    public boolean isAlreadyRecommended(BoardInfoEntity board, UserAuthEntity user) {
        return boardInfoRecommendRepository.existsByBoardAndUsernameAndIsActive(board, user, true);
    }

    public void saveRecommend(BoardInfoRecommendEntity entity) {
        boardInfoRecommendRepository.save(entity);
    }

    @Transactional
    public void increaseRecommendCount(Integer boardId) {
        boardInfoRepository.incrementRecommend(boardId);
    }
}
