package com.example.notfound_backend.data.dao.normalboard.recommend;

import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.normalboard.board.BoardFreeEntity;
import com.example.notfound_backend.data.entity.normalboard.recommend.BoardFreeRecommendEntity;
import com.example.notfound_backend.data.repository.normalboard.recommend.BoardFreeRecommendRepository;
import com.example.notfound_backend.data.repository.normalboard.board.BoardFreeRepository;
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
