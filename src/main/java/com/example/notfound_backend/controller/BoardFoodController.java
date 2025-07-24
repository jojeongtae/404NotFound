package com.example.notfound_backend.controller;


import com.example.notfound_backend.data.dto.BoardCommentDTO;
import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.data.dto.BoardRankingDTO;
import com.example.notfound_backend.data.entity.BoardFoodEntity;
import com.example.notfound_backend.service.BoardFoodCommentService;
import com.example.notfound_backend.service.BoardFoodRecommendService;
import com.example.notfound_backend.service.BoardFoodService;
import com.example.notfound_backend.service.BoardRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food")
public class BoardFoodController {
    private final BoardFoodService boardFoodService;
    private final BoardFoodCommentService boardFoodCommentService;
    private final BoardRankingService  boardRankingService;
    private final BoardFoodRecommendService boardFoodRecommendService;

    @GetMapping("/list")
    public List<BoardDTO> getAllBoards() {
        List<BoardDTO> boardDtoList = boardFoodService.findAll();
        System.out.println(boardDtoList.size());
        return boardDtoList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Integer id){
        BoardDTO dto= boardFoodService.viewBoard(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<BoardDTO> create(@RequestBody BoardDTO boardDTO) {
        BoardDTO created = boardFoodService.createBoard(boardDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDTO> update(@PathVariable Integer id, @RequestBody BoardDTO boardDTO) {
        BoardDTO updated = boardFoodService.updateBoard(id, boardDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boardFoodService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

//    @PatchMapping("/{id}/recommend")
//    public ResponseEntity<BoardDTO> recommend(@PathVariable Integer id) {
//        BoardDTO updated= boardFoodService.recommendBoard(id);
//        return ResponseEntity.ok(updated);
//    }
//
//    @PatchMapping("/{id}/cancel_recommend")
//    public ResponseEntity<BoardDTO> cancelRecommend(@PathVariable Integer id) {
//        BoardDTO updated= boardFoodService.cancelRecommendBoard(id);
//        return ResponseEntity.ok(updated);
//    }

    @GetMapping("/comments/{boardId}")
    public List<BoardCommentDTO> getComments(@PathVariable Integer boardId) {
        List<BoardCommentDTO> boardDtoList=boardFoodCommentService.getCommentsByBoardId(boardId);
        return boardDtoList;
    }

    @PostMapping("/comments/new")
    public ResponseEntity<BoardCommentDTO> addComment(@RequestBody BoardCommentDTO dto) {
        BoardCommentDTO created= boardFoodCommentService.addComment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<BoardCommentDTO> deleteComment(@PathVariable Integer id) {
        boardFoodCommentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ranking/comments")
    public List<BoardRankingDTO> getRankingByCommentsToday() {
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getFoodTop5ByCommentsToday();
        return boardRankingDTOList;
    }

    @GetMapping("/ranking/recommend")
    public List<BoardRankingDTO> getRecommendByRecommendToday() {
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getFoodTop5ByRecommendToday();
        return boardRankingDTOList;
    }

    @PostMapping("/{boardId}/recommend")
    public ResponseEntity<?> recommend(@PathVariable Integer boardId, Principal principal) {
        boardFoodRecommendService.recommend(boardId, principal.getName());
        return ResponseEntity.ok("추천 완료");
    }

//    @GetMapping("/search/title")
//    public ResponseEntity<List<BoardFoodEntity>> searchByTitle(@RequestParam("title") String title) {
//        List<BoardFoodEntity> results = boardFoodService.findByTitle(title);
//        return ResponseEntity.ok(results);
//    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BoardDTO>> searchByTitle(@RequestParam String title) {
        List<BoardDTO> boardDTOList = boardFoodService.findByTitle(title);
        return ResponseEntity.ok(boardDTOList);
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<BoardDTO>> searchByAuthor(@RequestParam String author) {
        List<BoardDTO> boardDTOList = boardFoodService.findByAuthor(author);
        return ResponseEntity.ok(boardDTOList);
    }

}