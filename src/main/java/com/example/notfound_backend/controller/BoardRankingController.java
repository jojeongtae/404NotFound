package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.BoardRankingDTO;
import com.example.notfound_backend.service.BoardRankingService;
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

    @GetMapping("/comments")
    public List<BoardRankingDTO> findTop5ByCommentsToday() {
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getAllBoardsTop5ByCommentsToday();
        return boardRankingDTOList;
    }

    @GetMapping("/recommend")
    public List<BoardRankingDTO> findTop5ByRecommend() {
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getAllBoardsTop5ByRecommendToday();
        return boardRankingDTOList;
    }
}
