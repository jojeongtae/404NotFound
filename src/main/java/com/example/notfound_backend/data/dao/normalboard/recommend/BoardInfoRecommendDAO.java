package com.example.notfound_backend.data.dao.normalboard.recommend;

import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.normalboard.board.BoardInfoEntity;
import com.example.notfound_backend.data.entity.normalboard.recommend.BoardInfoRecommendEntity;
import com.example.notfound_backend.data.repository.normalboard.recommend.BoardInfoRecommendRepository;
import com.example.notfound_backend.data.repository.normalboard.board.BoardInfoRepository;
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
