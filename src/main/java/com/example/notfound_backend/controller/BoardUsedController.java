package com.example.notfound_backend.controller;


import com.example.notfound_backend.data.dao.BoardUsedDAO;
import com.example.notfound_backend.data.dto.BoardCommentDTO;
import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.data.dto.BoardRankingDTO;
import com.example.notfound_backend.data.dto.UserAuthDTO;
import com.example.notfound_backend.data.entity.BoardUsedEntity;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/used")
public class BoardUsedController {
    private final BoardUsedService boardUsedService;
    private final BoardUsedCommentService boardUsedCommentService;
    private final BoardRankingService boardRankingService;
    private final BoardUsedRecommendService boardUsedRecommendService;

    @GetMapping("/list")
    public List<BoardDTO> getAllBoards() {
        List<BoardDTO> boardDtoList = boardUsedService.findAll();
        System.out.println(boardDtoList.size());
        return boardDtoList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Integer id){
        BoardDTO dto= boardUsedService.viewBoard(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<BoardDTO> create(@RequestPart("boardDTO") BoardDTO boardDTO,
                                           @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        BoardDTO created = boardUsedService.createBoard(boardDTO,file);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDTO> update(@PathVariable Integer id,
                                           @RequestPart("boardDTO") BoardDTO boardDTO,
                                           @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        BoardDTO updated = boardUsedService.updateBoard(id, boardDTO, file);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws IOException {
        boardUsedService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

//    @PatchMapping("/{id}/recommend")
//    public ResponseEntity<BoardDTO> recommend(@PathVariable Integer id) {
//        BoardDTO updated= boardUsedService.recommendBoard(id);
//        return ResponseEntity.ok(updated);
//    }
//
//    @PatchMapping("/{id}/cancel_recommend")
//    public ResponseEntity<BoardDTO> cancelRecommend(@PathVariable Integer id) {
//        BoardDTO updated= boardUsedService.cancelRecommendBoard(id);
//        return ResponseEntity.ok(updated);
//    }

    @GetMapping("/comments/{boardId}")
    public List<BoardCommentDTO> getComments(@PathVariable Integer boardId) {
        List<BoardCommentDTO> boardDtoList=boardUsedCommentService.getCommentsByBoardId(boardId);
        return boardDtoList;
    }

    @PostMapping("/comments/new")
    public ResponseEntity<BoardCommentDTO> addComment(@RequestBody BoardCommentDTO dto) {
        BoardCommentDTO created= boardUsedCommentService.addComment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<BoardCommentDTO> deleteComment(@PathVariable Integer id) {
        boardUsedCommentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ranking/comments")
    public List<BoardRankingDTO> getRankingByCommentsToday() {
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getUsedTop5ByCommentsToday();
        return boardRankingDTOList;
    }

    @GetMapping("/ranking/recommend")
    public List<BoardRankingDTO> getRecommendByRecommendToday() {
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getUsedTop5ByRecommendToday();
        return boardRankingDTOList;
    }

    @PostMapping("/{boardId}/recommend")
    public ResponseEntity<?> recommend(@PathVariable Integer boardId, Principal principal) {
        boardUsedRecommendService.recommend(boardId, principal.getName());
        return ResponseEntity.ok("추천 완료");
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BoardDTO>> searchByTitle(@RequestParam String title) {
        List<BoardDTO> boardDTOList = boardUsedService.findByTitle(title);
        return ResponseEntity.ok(boardDTOList);
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<BoardDTO>> searchByAuthor(@RequestParam String author) {
        List<BoardDTO> boardDTOList = boardUsedService.findByAuthor(author);
        return ResponseEntity.ok(boardDTOList);
    }
}
