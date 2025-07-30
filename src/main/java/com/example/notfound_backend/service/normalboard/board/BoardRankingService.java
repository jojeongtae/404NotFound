package com.example.notfound_backend.service.normalboard.board;

import com.example.notfound_backend.data.dao.normalboard.board.*;
import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
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

    // 각 게시판별 댓글 top5(최근 7일간)
    public List<BoardRankingDTO> getFreeTop5ByCommentsInLast7Days() {
        return boardFreeDAO.getTop5ByCommentsInLast7Days();
    }
    public List<BoardRankingDTO> getInfoTop5ByCommentsInLast7Days() {
        return boardInfoDAO.getTop5ByCommentsInLast7Days();
    }
    public List<BoardRankingDTO> getFoodTop5ByCommentsInLast7Days() {
        return boardFoodDAO.getTop5ByCommentsInLast7Days();
    }
    public List<BoardRankingDTO> getUsedTop5ByCommentsInLast7Days() {
        return boardUsedDAO.getTop5ByCommentsInLast7Days();
    }
    public List<BoardRankingDTO> getQnaTop5ByCommentsInLast7Days() {
        return boardQnaDAO.getTop5ByCommentsInLast7Days();
    }

    // 각 게시판별 추천 top5(최근 7일간)
    public List<BoardRankingDTO> getFreeTop5ByRecommendInLast7Days() {
        return boardFreeDAO.getTop5ByRecommendInLast7Days();
    }
    public List<BoardRankingDTO> getInfoTop5ByRecommendInLast7Days() {
        return boardInfoDAO.getTop5ByRecommendInLast7Days();
    }
    public List<BoardRankingDTO> getFoodTop5ByRecommendInLast7Days() {
        return boardFoodDAO.getTop5ByRecommendInLast7Days();
    }
    public List<BoardRankingDTO> getUsedTop5ByRecommendInLast7Days() {
        return boardUsedDAO.getTop5ByRecommendInLast7Days();
    }
    public List<BoardRankingDTO> getQnaTop5ByRecommendInLast7Days() {
        return boardQnaDAO.getTop5ByRecommendInLast7Days();
    }

    // 각 게시판별 추천 top5
    public List<BoardRankingDTO> getFreeTop5ByRecommend(){
        return boardFreeDAO.getTop5ByRecommend();
    }
    public List<BoardRankingDTO> getInfoTop5ByRecommend(){
        return boardInfoDAO.getTop5ByRecommend();
    }
    public List<BoardRankingDTO> getFoodTop5ByRecommend(){
        return boardFoodDAO.getTop5ByRecommend();
    }
    public List<BoardRankingDTO> getUsedTop5ByRecommend(){
        return boardUsedDAO.getTop5ByRecommend();
    }
    public List<BoardRankingDTO> getQnaTop5ByRecommend(){
        return boardQnaDAO.getTop5ByRecommend();
    }

    // 메인 화면에서 5개 게시판 전체 통합 댓글 top5
    public List<BoardRankingDTO> getAllBoardsTop5ByCommentsInLast7Days() {
        List<BoardRankingDTO> combined = new ArrayList<>();
        combined.addAll(getFreeTop5ByCommentsInLast7Days());
        combined.addAll(getInfoTop5ByCommentsInLast7Days());
        combined.addAll(getFoodTop5ByCommentsInLast7Days());
        combined.addAll(getUsedTop5ByCommentsInLast7Days());
        combined.addAll(getQnaTop5ByCommentsInLast7Days());

        // 댓글 수 내림차순 정렬 후 상위 5개
        return combined.stream()
                .sorted(Comparator.comparing(dto -> dto.getCommentCount()))
                .limit(5)
                .toList();
    }

    // 메인 화면에서 5개 게시판 전체 통합 추천 top5
    public List<BoardRankingDTO> getAllBoardsTop5ByRecommendInLast7Days() {
        List<BoardRankingDTO> combined = new ArrayList<>();
        combined.addAll(getFreeTop5ByRecommendInLast7Days());
        combined.addAll(getInfoTop5ByRecommendInLast7Days());
        combined.addAll(getFoodTop5ByRecommendInLast7Days());
        combined.addAll(getUsedTop5ByRecommendInLast7Days());
        combined.addAll(getQnaTop5ByRecommendInLast7Days());

        // 추천 수 내림차순 정렬 후 상위 5개
        return combined.stream()
                .sorted(Comparator.comparing(BoardRankingDTO::getRecommend,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(5)
                .toList();
    }

    public List<BoardRankingDTO> getAllBoardsTop5ByRecommend(){
        List<BoardRankingDTO> combined = new ArrayList<>();
        combined.addAll(getFreeTop5ByRecommend());
        combined.addAll(getInfoTop5ByRecommend());
        combined.addAll(getFoodTop5ByRecommend());
        combined.addAll(getUsedTop5ByRecommend());
        combined.addAll(getQnaTop5ByRecommend());

        return combined.stream()
                .sorted(Comparator.comparing(BoardRankingDTO::getRecommend,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(5)
                .toList();
    }
}
