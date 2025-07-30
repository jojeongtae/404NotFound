package com.example.notfound_backend.controller.normalboard;


import com.example.notfound_backend.data.dto.normalboard.BoardCommentDTO;
import com.example.notfound_backend.data.dto.normalboard.BoardDTO;
import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
import com.example.notfound_backend.service.normalboard.comment.BoardInfoCommentService;
import com.example.notfound_backend.service.normalboard.recommend.BoardInfoRecommendService;
import com.example.notfound_backend.service.normalboard.board.BoardInfoService;
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
@RequestMapping("/api/info")
public class BoardInfoController {
    private final BoardInfoService boardInfoService;
    private final BoardInfoCommentService boardInfoCommentService;
    private final BoardRankingService boardRankingService;
    private final BoardInfoRecommendService boardInfoRecommendService;

    // 외부인용 (VISIBLE만 조회)
    @GetMapping("/list")
    public List<BoardDTO> getAllBoards() {
        List<BoardDTO> boardDtoList = boardInfoService.findAll();
        System.out.println(boardDtoList.size());
        return boardDtoList;
    }
    // 유저용 (VISIBLE + 자신의 PRIVATE 조회)
    @GetMapping("/list/user")
    public ResponseEntity<List<BoardDTO>> getAllBoardsByUser(@RequestParam String username) {
        List<BoardDTO> boardDTOList = boardInfoService.findAllByUser(username);
        return ResponseEntity.ok(boardDTOList);
    }
    // 관리자용 (모든상태 게시글 조회)
    @GetMapping("/list/admin")
    public ResponseEntity<List<BoardDTO>> getAllBoardsByAdmin(@RequestParam String username) {
        List<BoardDTO> boardDTOList = boardInfoService.findAllByAdmin(username);
        return ResponseEntity.ok(boardDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Integer id){
        BoardDTO dto= boardInfoService.viewBoard(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<BoardDTO> create(@RequestPart("boardDTO") BoardDTO boardDTO,
                                           @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        BoardDTO created = boardInfoService.createBoard(boardDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDTO> update(@PathVariable Integer id,
                                           @RequestPart("boardDTO") BoardDTO boardDTO,
                                           @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        BoardDTO updated = boardInfoService.updateBoard(id, boardDTO, file);
        return ResponseEntity.ok(updated);
    }

    // 게시판 상태변경(본인, 관리자)
    @PutMapping("/status/{id}")
    public ResponseEntity<BoardDTO> updateStatus(@PathVariable Integer id, @RequestBody BoardDTO boardDTO) { // boardDTO(author,status)
        BoardDTO updatedBoardDTO = boardInfoService.updateBoardStatus(id, boardDTO);
        return ResponseEntity.ok(updatedBoardDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws IOException {
        boardInfoService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

//    @PatchMapping("/{id}/recommend")
//    public ResponseEntity<BoardDTO> recommend(@PathVariable Integer id) {
//        BoardDTO updated= boardInfoService.recommendBoard(id);
//        return ResponseEntity.ok(updated);
//    }
//
//    @PatchMapping("/{id}/cancel_recommend")
//    public ResponseEntity<BoardDTO> cancelRecommend(@PathVariable Integer id) {
//        BoardDTO updated= boardInfoService.cancelRecommendBoard(id);
//        return ResponseEntity.ok(updated);
//    }

    @GetMapping("/comments/{boardId}")
    public List<BoardCommentDTO> getComments(@PathVariable Integer boardId) {
        List<BoardCommentDTO> boardDtoList=boardInfoCommentService.getCommentsByBoardId(boardId);
        return boardDtoList;
    }

    @PostMapping("/comments/new")
    public ResponseEntity<BoardCommentDTO> addComment(@RequestBody BoardCommentDTO dto) {
        BoardCommentDTO created= boardInfoCommentService.addComment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<BoardCommentDTO> deleteComment(@PathVariable Integer id) {
        boardInfoCommentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ranking/comments")
    public List<BoardRankingDTO> getRankingByCommentsToday() {
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getInfoTop5ByCommentsInLast7Days();
        return boardRankingDTOList;
    }

    @GetMapping("/ranking/recommend")
    public List<BoardRankingDTO> getRecommendByRecommendToday() {
        List<BoardRankingDTO> boardRankingDTOList=boardRankingService.getInfoTop5ByRecommendInLast7Days();
        return boardRankingDTOList;
    }

    @PostMapping("/{boardId}/recommend")
    public ResponseEntity<?> recommend(@PathVariable Integer boardId, Principal principal) {
        boardInfoRecommendService.recommend(boardId, principal.getName());
        return ResponseEntity.ok("추천 완료");
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BoardDTO>> findByTitle(@RequestParam String title) {
        List<BoardDTO> boardDTOList=boardInfoService.findByTitle(title);
        return ResponseEntity.ok(boardDTOList);
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<BoardDTO>> findByAuthor(@RequestParam String author) {
        List<BoardDTO> boardDTOList=boardInfoService.findByTitle(author);
        return ResponseEntity.ok(boardDTOList);
    }
}
