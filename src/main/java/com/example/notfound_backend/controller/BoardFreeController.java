package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.data.dto.BoardCommentDTO;
import com.example.notfound_backend.data.dto.BoardRankingDTO;
import com.example.notfound_backend.service.BoardFreeCommentService;
import com.example.notfound_backend.service.BoardFreeRecommendService;
import com.example.notfound_backend.service.BoardFreeService;
import com.example.notfound_backend.service.BoardRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/free")
public class BoardFreeController {
    private final BoardFreeService boardFreeService;
    private final BoardFreeCommentService boardFreeCommentService;
    private final BoardRankingService boardRankingService;
    private final BoardFreeRecommendService boardFreeRecommendService;

    @GetMapping("/list")
    public List<BoardDTO> getAllBoards() {
        List<BoardDTO> boardDtoList = boardFreeService.findAll();
        System.out.println(boardDtoList.size());
        return boardDtoList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Integer id){
        BoardDTO dto= boardFreeService.viewBoard(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<BoardDTO> create(@RequestPart("boardDTO") BoardDTO boardDTO,
                                           @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        BoardDTO created = boardFreeService.createBoard(boardDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDTO> update(@PathVariable Integer id,
                                           @RequestPart("boardDTO") BoardDTO boardDTO,
                                           @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        BoardDTO updated = boardFreeService.updateBoard(id, boardDTO, file);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws IOException {
        boardFreeService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

//    @PatchMapping("/{id}/recommend")
//    public ResponseEntity<BoardDTO> recommend(@PathVariable Integer id) {
//        BoardDTO updated= boardFreeService.recommendBoard(id);
//        return ResponseEntity.ok(updated);
//    }
//
//    @PatchMapping("/{id}/cancel_recommend")
//    public ResponseEntity<BoardDTO> cancelRecommend(@PathVariable Integer id) {
//        BoardDTO updated= boardFreeService.cancelRecommendBoard(id);
//        return ResponseEntity.ok(updated);
//    }

    @GetMapping("/comments/{boardId}")
    public List<BoardCommentDTO> getComments(@PathVariable Integer boardId) {
        List<BoardCommentDTO> boardDtoList=boardFreeCommentService.getCommentsByBoardId(boardId);
        return boardDtoList;
    }

    @PostMapping("/comments/new")
    public ResponseEntity<BoardCommentDTO> addComment(@RequestBody BoardCommentDTO dto) {
        BoardCommentDTO created= boardFreeCommentService.addComment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<BoardCommentDTO> deleteComment(@PathVariable Integer id) {
        boardFreeCommentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ranking/comments")
    public List<BoardRankingDTO> getRankingByCommentsToday() {
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getFreeTop5ByCommentsToday();
        return boardRankingDTOList;
    }

    @GetMapping("/ranking/recommend")
    public List<BoardRankingDTO> getRecommendByRecommendToday() {
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getFreeTop5ByRecommendToday();
        return boardRankingDTOList;
    }

    @PostMapping("/{boardId}/recommend")
    public ResponseEntity<?> recommend(@PathVariable Integer boardId, Principal principal) {
        boardFreeRecommendService.recommend(boardId, principal.getName());
        return ResponseEntity.ok("추천 완료");
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BoardDTO>> searchByTitle(@RequestParam String title) {
        List<BoardDTO> boardDTOList=boardFreeService.findByTitle(title);
        return ResponseEntity.ok(boardDTOList);
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<BoardDTO>> searchByAuthor(@RequestParam String author) {
        List<BoardDTO> boardDTOList=boardFreeService.findByAuthor(author);
        return ResponseEntity.ok(boardDTOList);
    }

}
