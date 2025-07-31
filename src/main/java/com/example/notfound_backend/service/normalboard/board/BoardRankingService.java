package com.example.notfound_backend.service.normalboard.board;

import com.example.notfound_backend.data.dao.normalboard.board.*;
import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
import com.example.notfound_backend.data.dto.normalboard.BoardRankingResponse;
import com.example.notfound_backend.service.admin.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardRankingService {
    private final BoardFoodDAO boardFoodDAO;
    private final BoardFreeDAO boardFreeDAO;
    private final BoardQnaDAO boardQnaDAO;
    private final BoardInfoDAO boardInfoDAO;
    private final BoardUsedDAO boardUsedDAO;
    private final UserInfoService userInfoService;

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

    //각 게시판별 조회수 top5(최근 7일간)
    public List<BoardRankingDTO> getFreeTop5ByViews() { return boardFreeDAO.getTop5ByViewsInLast7Days(); }
    public List<BoardRankingDTO> getInfoTop5ByViews() { return boardInfoDAO.getTop5ByViewsInLast7Days(); }
    public List<BoardRankingDTO> getFoodTop5ByViews() { return boardFoodDAO.getTop5ByViewsInLast7Days(); }
    public List<BoardRankingDTO> getUsedTop5ByViews() { return boardUsedDAO.getTop5ByViewsInLast7Days(); }
    public List<BoardRankingDTO> getQnaTop5ByViews() { return boardQnaDAO.getTop5ByViewsInLast7Days(); }

    //전체 게시판 추천 랭킹
    public List<BoardRankingResponse> getAllBoardsTop5ByRecommend(){
        List<BoardRankingDTO> combined=new ArrayList<>();
        combined.addAll(getFreeTop5ByRecommend());
        combined.addAll(getInfoTop5ByRecommend());
        combined.addAll(getFoodTop5ByRecommend());
        combined.addAll(getUsedTop5ByRecommend());
        combined.addAll(getQnaTop5ByRecommend());

        List<BoardRankingResponse> result=combined.stream()
                .map(dto -> new BoardRankingResponse(
                        dto.getId(),
                        dto.getTitle(),
                        dto.getAuthor(),
                        dto.getRecommend(),
                        dto.getViews(),
                        dto.getCategory(),
                        dto.getCreatedAt(),
                        dto.getCommentCount(),
                        dto.getAuthorNickname(),
                        userInfoService.getUserGrade(dto.getAuthor())
                ))
                .sorted(Comparator.comparing(BoardRankingResponse::getRecommend,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(5)
                .collect(Collectors.toList());

        return result;
    }

    //전체 게시판 주간 추천 랭킹
    public List<BoardRankingResponse> getAllBoardsTop5ByRecommendInLast7Days() {
        List<BoardRankingDTO> combined = new ArrayList<>();
        combined.addAll(getFreeTop5ByRecommendInLast7Days());
        combined.addAll(getInfoTop5ByRecommendInLast7Days());
        combined.addAll(getFoodTop5ByRecommendInLast7Days());
        combined.addAll(getUsedTop5ByRecommendInLast7Days());
        combined.addAll(getQnaTop5ByRecommendInLast7Days());

        List<BoardRankingResponse> result = combined.stream()
                .map(dto -> new BoardRankingResponse(
                        dto.getId(),
                        dto.getTitle(),
                        dto.getAuthor(),
                        dto.getRecommend(),
                        dto.getViews(),
                        dto.getCategory(),
                        dto.getCreatedAt(),
                        dto.getCommentCount(),
                        dto.getAuthorNickname(),
                        userInfoService.getUserGrade(dto.getAuthor())
                ))
                .sorted(Comparator.comparing(BoardRankingResponse::getRecommend,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(5)
                .collect(Collectors.toList());

        return result;
    }

    //전체 게시판 주간 댓글 랭킹
    public List<BoardRankingResponse> getAllBoardsTop5ByCommentsInLast7Days() {
        List<BoardRankingDTO> combined = new ArrayList<>();
        combined.addAll(getFreeTop5ByCommentsInLast7Days());
        combined.addAll(getInfoTop5ByCommentsInLast7Days());
        combined.addAll(getFoodTop5ByCommentsInLast7Days());
        combined.addAll(getUsedTop5ByCommentsInLast7Days());
        combined.addAll(getQnaTop5ByCommentsInLast7Days());

        List<BoardRankingResponse> result = combined.stream()
                .map(dto -> new BoardRankingResponse(
                        dto.getId(),
                        dto.getTitle(),
                        dto.getAuthor(),
                        dto.getRecommend(),
                        dto.getViews(),
                        dto.getCategory(),
                        dto.getCreatedAt(),
                        dto.getCommentCount(),
                        dto.getAuthorNickname(),
                        userInfoService.getUserGrade(dto.getAuthor())
                ))
                .sorted(Comparator.comparing(BoardRankingResponse::getCommentCount,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(5)
                .collect(Collectors.toList());

        return result;
    }

    public List<BoardRankingResponse> getAllBoardsTop5ByViewsInLast7Days(){
        List<BoardRankingDTO> combined = new ArrayList<>();
        combined.addAll(getFreeTop5ByViews());
        combined.addAll(getInfoTop5ByViews());
        combined.addAll(getFoodTop5ByViews());
        combined.addAll(getUsedTop5ByViews());
        combined.addAll(getQnaTop5ByViews());

        List<BoardRankingResponse> result=combined.stream()
                .map(dto -> new BoardRankingResponse(
                        dto.getId(),
                        dto.getTitle(),
                        dto.getAuthor(),
                        dto.getRecommend(),
                        dto.getViews(),
                        dto.getCategory(),
                        dto.getCreatedAt(),
                        dto.getCommentCount(),
                        dto.getAuthorNickname(),
                        userInfoService.getUserGrade(dto.getAuthor())
                ))
                .sorted(Comparator.comparing(BoardRankingResponse::getViews,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(5)
                .collect(Collectors.toList());
        return result;
    }
}
