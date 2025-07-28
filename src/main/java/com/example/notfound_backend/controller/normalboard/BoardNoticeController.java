package com.example.notfound_backend.controller.normalboard;

import com.example.notfound_backend.data.dto.normalboard.BoardCommentDTO;
import com.example.notfound_backend.data.dto.normalboard.BoardDTO;
import com.example.notfound_backend.service.normalboard.comment.BoardNoticeCommentService;
import com.example.notfound_backend.service.normalboard.recommend.BoardNoticeRecommendService;
import com.example.notfound_backend.service.normalboard.board.BoardNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class BoardNoticeController {
    private final BoardNoticeService boardNoticeService;
    private final BoardNoticeCommentService boardNoticeCommentService;
    private final BoardNoticeRecommendService boardNoticeRecommendService;

    @GetMapping("/list")
    public List<BoardDTO> getAllBoards() {
        List<BoardDTO> boardDtoList = boardNoticeService.findAll();
        System.out.println(boardDtoList.size());
        return boardDtoList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Integer id){
        BoardDTO dto= boardNoticeService.viewBoard(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<BoardDTO> create(@RequestPart("boardDTO") BoardDTO boardDTO,
                                           @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        BoardDTO created = boardNoticeService.createBoard(boardDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDTO> update(@PathVariable Integer id,
                                           @RequestPart("boardDTO") BoardDTO boardDTO,
                                           @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        BoardDTO updated = boardNoticeService.updateBoard(id, boardDTO, file);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id,
                                       @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        String username = userDetails.getUsername(); // 현재 로그인한 사용자 ID
        boardNoticeService.deleteBoard(id, username);
        return ResponseEntity.noContent().build();
    }

//    @PatchMapping("/{id}/recommend")
//    public ResponseEntity<BoardDTO> recommend(@PathVariable Integer id) {
//        BoardDTO updated= boardNoticeService.recommendBoard(id);
//        return ResponseEntity.ok(updated);
//    }
//
//    @PatchMapping("/{id}/cancel_recommend")
//    public ResponseEntity<BoardDTO> cancelRecommend(@PathVariable Integer id) {
//        BoardDTO updated= boardNoticeService.cancelRecommendBoard(id);
//        return ResponseEntity.ok(updated);
//    }

    @GetMapping("/comments/{boardId}")
    public List<BoardCommentDTO> getComments(@PathVariable Integer boardId) {
        List<BoardCommentDTO> boardDtoList=boardNoticeCommentService.getCommentsByBoardId(boardId);
        return boardDtoList;
    }

    @PostMapping("/comments/new")
    public ResponseEntity<BoardCommentDTO> addComment(@RequestBody BoardCommentDTO dto) {
        BoardCommentDTO created= boardNoticeCommentService.addComment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<BoardCommentDTO> deleteComment(@PathVariable Integer id) {
        boardNoticeCommentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{boardId}/recommend")
    public ResponseEntity<?> recommend(@PathVariable Integer boardId, Principal principal) {
        boardNoticeRecommendService.recommend(boardId, principal.getName());
        return ResponseEntity.ok("추천 완료");
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BoardDTO>> searchByTitle(@RequestParam String title) {
        List<BoardDTO> boardDTOList = boardNoticeService.findByTitle(title);
        return ResponseEntity.ok(boardDTOList);
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<BoardDTO>> searchByAuthor(@RequestParam String author) {
        List<BoardDTO> boardDTOList = boardNoticeService.findByAuthor(author);
        return  ResponseEntity.ok(boardDTOList);
    }
}
