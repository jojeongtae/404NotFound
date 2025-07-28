package com.example.notfound_backend.data.dao.normalboard.recommend;

import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.normalboard.board.BoardQnaEntity;
import com.example.notfound_backend.data.entity.normalboard.recommend.BoardQnaRecommendEntity;
import com.example.notfound_backend.data.repository.normalboard.recommend.BoardQnaRecommendRepository;
import com.example.notfound_backend.data.repository.normalboard.board.BoardQnaRepository;
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
