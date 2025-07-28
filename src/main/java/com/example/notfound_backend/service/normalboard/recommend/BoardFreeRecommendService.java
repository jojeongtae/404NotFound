package com.example.notfound_backend.service.normalboard.recommend;

import com.example.notfound_backend.data.dao.normalboard.board.BoardFreeDAO;
import com.example.notfound_backend.data.dao.normalboard.recommend.BoardFreeRecommendDAO;
import com.example.notfound_backend.data.dao.login.UserAuthDAO;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.normalboard.board.BoardFreeEntity;
import com.example.notfound_backend.data.entity.normalboard.recommend.BoardFreeRecommendEntity;
import com.example.notfound_backend.service.admin.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BoardFreeRecommendService {
    private final BoardFreeRecommendDAO boardFreeRecommendDAO;
    private final BoardFreeDAO boardFreeDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoService userInfoService;

    public void recommend(Integer boardId, String username) {
        userInfoService.userStatusValidator(username);

        BoardFreeEntity board = boardFreeDAO.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        UserAuthEntity user = userAuthDAO.findByUsername(username);

        if (boardFreeRecommendDAO.isAlreadyRecommended(board, user)) {
            throw new IllegalStateException("이미 추천했습니다.");
        }

        BoardFreeRecommendEntity entity = new BoardFreeRecommendEntity();
        entity.setBoard(board);
        entity.setUsername(user);
        entity.setIsActive(true);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        boardFreeRecommendDAO.saveRecommend(entity);
        boardFreeRecommendDAO.increaseRecommendCount(board.getId());
    }
}
