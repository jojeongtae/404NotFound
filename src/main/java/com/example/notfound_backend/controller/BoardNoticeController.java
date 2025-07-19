package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.service.BoardNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class BoardNoticeController {
    private final BoardNoticeService boardNoticeService;

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
    public ResponseEntity<BoardDTO> create(@RequestBody BoardDTO boardDTO) {
        BoardDTO created = boardNoticeService.createBoard(boardDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDTO> update(@PathVariable Integer id, @RequestBody BoardDTO boardDTO) {
        BoardDTO updated = boardNoticeService.updateBoard(id, boardDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername(); // 현재 로그인한 사용자 ID
        boardNoticeService.deleteBoard(id, username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/recommend")
    public ResponseEntity<BoardDTO> recommend(@PathVariable Integer id) {
        BoardDTO updated= boardNoticeService.recommendBoard(id);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/cancel_recommend")
    public ResponseEntity<BoardDTO> cancelRecommend(@PathVariable Integer id) {
        BoardDTO updated= boardNoticeService.cancelRecommendBoard(id);
        return ResponseEntity.ok(updated);
    }
}
