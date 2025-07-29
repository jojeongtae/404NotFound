package com.example.notfound_backend.data.dao.normalboard.recommend;

import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.normalboard.board.BoardNoticeEntity;
import com.example.notfound_backend.data.entity.normalboard.recommend.BoardNoticeRecommendEntity;
import com.example.notfound_backend.data.repository.normalboard.recommend.BoardNoticeRecommendRepository;
import com.example.notfound_backend.data.repository.normalboard.board.BoardNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardNoticeRecommendDAO {
    private final BoardNoticeRecommendRepository boardNoticeRecommendRepository;
    private final BoardNoticeRepository boardNoticeRepository;

    public boolean isAlreadyRecommended(BoardNoticeEntity board, UserAuthEntity user) {
        return boardNoticeRecommendRepository.existsByBoardAndUsernameAndIsActive(board, user, true);
    }

    public void saveRecommend(BoardNoticeRecommendEntity entity) {
        boardNoticeRecommendRepository.save(entity);
    }

    @Transactional
    public void increaseRecommendCount(Integer boardId) {
        boardNoticeRepository.incrementRecommend(boardId);
    }
}
