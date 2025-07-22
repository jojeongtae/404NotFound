package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.*;
import com.example.notfound_backend.data.dto.BoardRankingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardRankingService {
    private final BoardFoodDAO boardFoodDAO;
    private final BoardFreeDAO boardFreeDAO;
    private final BoardQnaDAO boardQnaDAO;
    private final BoardInfoDAO boardInfoDAO;
    private final BoardUsedDAO boardUsedDAO;

    // 각 게시판별 댓글 top5
    public List<BoardRankingDTO> getFreeTop5ByCommentsToday() {
        return boardFreeDAO.getTop5ByCommentsToday();
    }
    public List<BoardRankingDTO> getInfoTop5ByCommentsToday() {
        return boardInfoDAO.getTop5ByCommentsToday();
    }
    public List<BoardRankingDTO> getFoodTop5ByCommentsToday() {
        return boardFoodDAO.getTop5ByCommentsToday();
    }
    public List<BoardRankingDTO> getUsedTop5ByCommentsToday() {
        return boardUsedDAO.getTop5ByCommentsToday();
    }
    public List<BoardRankingDTO> getQnaTop5ByCommentsToday() {
        return boardQnaDAO.getTop5ByCommentsToday();
    }

    // 각 게시판별 추천 top5
    public List<BoardRankingDTO> getFreeTop5ByRecommendToday() {
        return boardFreeDAO.getTop5ByRecommendToday();
    }
    public List<BoardRankingDTO> getInfoTop5ByRecommendToday() {
        return boardInfoDAO.getTop5ByRecommendToday();
    }
    public List<BoardRankingDTO> getFoodTop5ByRecommendToday() {
        return boardFoodDAO.getTop5ByRecommendToday();
    }
    public List<BoardRankingDTO> getUsedTop5ByRecommendToday() {
        return boardUsedDAO.getTop5ByRecommendToday();
    }
    public List<BoardRankingDTO> getQnaTop5ByRecommendToday() {
        return boardQnaDAO.getTop5ByRecommendToday();
    }

    // 메인 화면에서 5개 게시판 전체 통합 댓글 top5
    public List<BoardRankingDTO> getAllBoardsTop5ByCommentsToday() {
        List<BoardRankingDTO> combined = new ArrayList<>();
        combined.addAll(getFreeTop5ByCommentsToday());
        combined.addAll(getInfoTop5ByCommentsToday());
        combined.addAll(getFoodTop5ByCommentsToday());
        combined.addAll(getUsedTop5ByCommentsToday());
        combined.addAll(getQnaTop5ByCommentsToday());

        // 댓글 수 내림차순 정렬 후 상위 5개
        return combined.stream()
                .sorted(Comparator.comparing(dto -> dto.getCommentCount()))
                .limit(5)
                .toList();
    }

    // 메인 화면에서 5개 게시판 전체 통합 추천 top5
    public List<BoardRankingDTO> getAllBoardsTop5ByRecommendToday() {
        List<BoardRankingDTO> combined = new ArrayList<>();
        combined.addAll(getFreeTop5ByRecommendToday());
        combined.addAll(getInfoTop5ByRecommendToday());
        combined.addAll(getFoodTop5ByRecommendToday());
        combined.addAll(getUsedTop5ByRecommendToday());
        combined.addAll(getQnaTop5ByRecommendToday());

        // 추천 수 내림차순 정렬 후 상위 5개
        return combined.stream()
                .sorted(Comparator.comparing(BoardRankingDTO::getRecommend).reversed())
                .limit(5)
                .toList();
    }
}
