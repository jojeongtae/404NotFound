package com.example.notfound_backend.controller.normalboard;

import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
import com.example.notfound_backend.service.normalboard.board.BoardRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranking")
public class BoardRankingController {
    private final BoardRankingService boardRankingService;

    @GetMapping("/comments/week")
    public List<BoardRankingDTO> findTop5ByCommentsInLast7Days() {
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getAllBoardsTop5ByCommentsInLast7Days();
        return boardRankingDTOList;
    }

    @GetMapping("/recommend/week")
    public List<BoardRankingDTO> findTop5ByRecommendInLast7Days() {
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getAllBoardsTop5ByRecommendInLast7Days();
        return boardRankingDTOList;
    }

    @GetMapping("/recommend/all")
    public List<BoardRankingDTO> findTop5ByRecommend(){
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getAllBoardsTop5ByRecommend();
        return boardRankingDTOList;
    }
}
