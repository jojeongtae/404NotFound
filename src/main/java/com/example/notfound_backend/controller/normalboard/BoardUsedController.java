package com.example.notfound_backend.controller.normalboard;


import com.example.notfound_backend.data.dto.normalboard.BoardCommentDTO;
import com.example.notfound_backend.data.dto.normalboard.BoardDTO;
import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
import com.example.notfound_backend.data.dto.normalboard.BoardUsedDTO;
import com.example.notfound_backend.service.normalboard.board.BoardRankingService;
import com.example.notfound_backend.service.normalboard.board.BoardUsedService;
import com.example.notfound_backend.service.normalboard.comment.BoardUsedCommentService;
import com.example.notfound_backend.service.normalboard.recommend.BoardUsedRecommendService;
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
@RequestMapping("/api/used")
public class BoardUsedController {
    private final BoardUsedService boardUsedService;
    private final BoardUsedCommentService boardUsedCommentService;
    private final BoardRankingService boardRankingService;
    private final BoardUsedRecommendService boardUsedRecommendService;

    // 외부인용 (VISIBLE만 조회)
    @GetMapping("/list")
    public List<BoardUsedDTO> getAllBoards() {
        List<BoardUsedDTO> boardDtoList = boardUsedService.findAll();
        System.out.println(boardDtoList.size());
        return boardDtoList;
    }
    // 유저용 (VISIBLE + 자신의 PRIVATE 조회)
    @GetMapping("/list/user")
    public ResponseEntity<List<BoardUsedDTO>> getAllBoardsByUser(@RequestParam String username) {
        List<BoardUsedDTO> boardDTOList = boardUsedService.findAllByUser(username);
        return ResponseEntity.ok(boardDTOList);
    }
    // 관리자용 (모든상태 게시글 조회)
    @GetMapping("/list/admin")
    public ResponseEntity<List<BoardUsedDTO>> getAllBoardsByAdmin(@RequestParam String username) {
        List<BoardUsedDTO> boardDTOList = boardUsedService.findAllByAdmin(username);
        return ResponseEntity.ok(boardDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardUsedDTO> getBoard(@PathVariable Integer id){
        BoardUsedDTO dto= boardUsedService.viewBoard(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<BoardUsedDTO> create(@RequestPart("boardDTO") BoardUsedDTO boardDTO,
                                           @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        BoardUsedDTO created = boardUsedService.createBoard(boardDTO,file);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardUsedDTO> update(@PathVariable Integer id,
                                               @RequestPart("boardDTO") BoardUsedDTO boardDTO,
                                               @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        BoardUsedDTO updated = boardUsedService.updateBoard(id, boardDTO, file);
        return ResponseEntity.ok(updated);
    }

    // 게시판 상태변경(본인, 관리자)
    @PutMapping("/status/{id}")
    public ResponseEntity<BoardUsedDTO> updateStatus(@PathVariable Integer id, @RequestBody BoardUsedDTO boardUsedDTO) { // boardUsedDTO(author,status)
        BoardUsedDTO updatedBoardUsedDTO = boardUsedService.updateBoardStatus(id, boardUsedDTO);
        return ResponseEntity.ok(updatedBoardUsedDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws IOException {
        boardUsedService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

//    @PatchMapping("/{id}/recommend")
//    public ResponseEntity<BoardUsedDTO
//    > recommend(@PathVariable Integer id) {
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
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getUsedTop5ByCommentsInLast7Days();
        return boardRankingDTOList;
    }

    @GetMapping("/ranking/recommend")
    public List<BoardRankingDTO> getRecommendByRecommendToday() {
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getUsedTop5ByRecommendInLast7Days();
        return boardRankingDTOList;
    }

    @PostMapping("/{boardId}/recommend")
    public ResponseEntity<?> recommend(@PathVariable Integer boardId, Principal principal) {
        boardUsedRecommendService.recommend(boardId, principal.getName());
        return ResponseEntity.ok("추천 완료");
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BoardUsedDTO>> searchByTitle(@RequestParam String title) {
        List<BoardUsedDTO> boardDTOList = boardUsedService.findByTitle(title);
        return ResponseEntity.ok(boardDTOList);
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<BoardUsedDTO>> searchByAuthor(@RequestParam String author) {
        List<BoardUsedDTO> boardDTOList = boardUsedService.findByAuthor(author);
        return ResponseEntity.ok(boardDTOList);
    }
}
