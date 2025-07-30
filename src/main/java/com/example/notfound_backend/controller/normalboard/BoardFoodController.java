package com.example.notfound_backend.controller.normalboard;


import com.example.notfound_backend.data.dto.normalboard.BoardCommentDTO;
import com.example.notfound_backend.data.dto.normalboard.BoardDTO;
import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
import com.example.notfound_backend.service.normalboard.comment.BoardFoodCommentService;
import com.example.notfound_backend.service.normalboard.recommend.BoardFoodRecommendService;
import com.example.notfound_backend.service.normalboard.board.BoardFoodService;
import com.example.notfound_backend.service.normalboard.board.BoardRankingService;
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
@RequestMapping("/api/food")
public class BoardFoodController {
    private final BoardFoodService boardFoodService;
    private final BoardFoodCommentService boardFoodCommentService;
    private final BoardRankingService  boardRankingService;
    private final BoardFoodRecommendService boardFoodRecommendService;

    // 외부인용 (VISIBLE만 조회)
    @GetMapping("/list")
    public List<BoardDTO> getAllBoards() {
        List<BoardDTO> boardDtoList = boardFoodService.findAll();
        System.out.println(boardDtoList.size());
        return boardDtoList;
    }
    // 유저용 (VISIBLE + 자신의 PRIVATE 조회)
    @GetMapping("/list/user")
    public ResponseEntity<List<BoardDTO>> getAllBoardsByUser(@RequestParam String username) {
        List<BoardDTO> boardDTOList = boardFoodService.findAllByUser(username);
        return ResponseEntity.ok(boardDTOList);
    }
    // 관리자용 (모든상태 게시글 조회)
    @GetMapping("/list/admin")
    public ResponseEntity<List<BoardDTO>> getAllBoardsByAdmin(@RequestParam String username) {
        List<BoardDTO> boardDTOList = boardFoodService.findAllByAdmin(username);
        return ResponseEntity.ok(boardDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Integer id){
        BoardDTO dto= boardFoodService.viewBoard(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<BoardDTO> create(@RequestPart("boardDTO") BoardDTO boardDTO,
                                           @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        BoardDTO created = boardFoodService.createBoard(boardDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDTO> update(@PathVariable Integer id,
                                           @RequestPart("boardDTO") BoardDTO boardDTO,
                                           @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        BoardDTO updated = boardFoodService.updateBoard(id, boardDTO, file);
        return ResponseEntity.ok(updated);
    }

    // 게시판 상태변경(본인, 관리자)
    @PutMapping("/status/{id}")
    public ResponseEntity<BoardDTO> updateStatus(@PathVariable Integer id, @RequestBody BoardDTO boardDTO) { // boardDTO(author,status)
        BoardDTO updatedBoardDTO = boardFoodService.updateBoardStatus(id, boardDTO);
        return ResponseEntity.ok(updatedBoardDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws IOException {
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
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getFoodTop5ByCommentsInLast7Days();
        return boardRankingDTOList;
    }

    @GetMapping("/ranking/recommend")
    public List<BoardRankingDTO> getRecommendByRecommendToday() {
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getFoodTop5ByRecommendInLast7Days();
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