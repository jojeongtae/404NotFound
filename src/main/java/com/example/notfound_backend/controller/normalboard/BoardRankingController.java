package com.example.notfound_backend.controller.normalboard;

import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
import com.example.notfound_backend.data.dto.normalboard.BoardRankingResponse;
import com.example.notfound_backend.service.normalboard.board.BoardRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranking")
public class BoardRankingController {
    private final BoardRankingService boardRankingService;

    @GetMapping("/recommend/all")
    public List<BoardRankingResponse> getAllBoardsTop5ByRecommend(){
        List<BoardRankingResponse> rankingList=boardRankingService.getAllBoardsTop5ByRecommend();
        return rankingList;
    }

    @GetMapping("/recommend/week")
    public List<BoardRankingResponse> getAllBoardsTop5ByRecommendInLast7Days() {
        List<BoardRankingResponse> rankingList = boardRankingService.getAllBoardsTop5ByRecommendInLast7Days();
        return rankingList;
    }

    @GetMapping("/comments/week")
    public List<BoardRankingResponse> getAllBoardsTop5ByCommentsInLast7Days() {
        List<BoardRankingResponse> rankingList=boardRankingService.getAllBoardsTop5ByRecommendInLast7Days();
        return rankingList;
    }

    @GetMapping("/views/week")
    public List<BoardRankingResponse> getAllBoardsTop5ByViewsInLast7Days() {
        List<BoardRankingResponse> rankingList=boardRankingService.getAllBoardsTop5ByViewsInLast7Days();
        return rankingList;
    }
}
