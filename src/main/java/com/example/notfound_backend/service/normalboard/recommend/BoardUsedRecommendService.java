package com.example.notfound_backend.service.normalboard.recommend;

import com.example.notfound_backend.data.dao.normalboard.board.BoardUsedDAO;
import com.example.notfound_backend.data.dao.normalboard.recommend.BoardUsedRecommendDAO;
import com.example.notfound_backend.data.dao.login.UserAuthDAO;
import com.example.notfound_backend.data.entity.normalboard.board.BoardUsedEntity;
import com.example.notfound_backend.data.entity.normalboard.recommend.BoardUsedRecommendEntity;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.service.admin.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BoardUsedRecommendService {
    private final BoardUsedRecommendDAO  boardUsedRecommendDAO;
    private final BoardUsedDAO boardUsedDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoService userInfoService;

    public void recommend(Integer boardId, String username) {
        userInfoService.userStatusValidator(username);

        BoardUsedEntity board = boardUsedDAO.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        UserAuthEntity user = userAuthDAO.findByUsername(username);

        if (boardUsedRecommendDAO.isAlreadyRecommended(board, user)) {
            throw new IllegalStateException("이미 추천했습니다.");
        }

        BoardUsedRecommendEntity entity = new BoardUsedRecommendEntity();
        entity.setBoard(board);
        entity.setUsername(user);
        entity.setIsActive(true);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        boardUsedRecommendDAO.saveRecommend(entity);
        boardUsedRecommendDAO.increaseRecommendCount(board.getId());
    }

}
