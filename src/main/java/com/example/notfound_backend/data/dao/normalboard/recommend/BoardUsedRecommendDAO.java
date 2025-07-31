package com.example.notfound_backend.data.dao.normalboard.recommend;

import com.example.notfound_backend.data.entity.normalboard.board.BoardUsedEntity;
import com.example.notfound_backend.data.entity.normalboard.recommend.BoardUsedRecommendEntity;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.repository.normalboard.recommend.BoardUsedRecommendRepository;
import com.example.notfound_backend.data.repository.normalboard.board.BoardUsedRepository;
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
