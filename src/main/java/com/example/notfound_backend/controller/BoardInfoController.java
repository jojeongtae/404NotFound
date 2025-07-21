package com.example.notfound_backend.controller;


import com.example.notfound_backend.data.dto.BoardCommentDTO;
import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.service.BoardInfoCommentService;
import com.example.notfound_backend.service.BoardInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/info")
public class BoardInfoController {
    private final BoardInfoService boardInfoService;
    private final BoardInfoCommentService boardInfoCommentService;

    @GetMapping("/list")
    public List<BoardDTO> getAllBoards() {
        List<BoardDTO> boardDtoList = boardInfoService.findAll();
        System.out.println(boardDtoList.size());
        return boardDtoList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Integer id){
        BoardDTO dto= boardInfoService.viewBoard(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<BoardDTO> create(@RequestBody BoardDTO boardDTO) {
        BoardDTO created = boardInfoService.createBoard(boardDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDTO> update(@PathVariable Integer id, @RequestBody BoardDTO boardDTO) {
        BoardDTO updated = boardInfoService.updateBoard(id, boardDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boardInfoService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/recommend")
    public ResponseEntity<BoardDTO> recommend(@PathVariable Integer id) {
        BoardDTO updated= boardInfoService.recommendBoard(id);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/cancel_recommend")
    public ResponseEntity<BoardDTO> cancelRecommend(@PathVariable Integer id) {
        BoardDTO updated= boardInfoService.cancelRecommendBoard(id);
        return ResponseEntity.ok(updated);
    }

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

    @DeleteMapping("/comments/delete")
    public ResponseEntity<BoardCommentDTO> deleteComment(@RequestBody BoardCommentDTO dto) {
        boardInfoCommentService.deleteComment(dto.getId());
        return ResponseEntity.noContent().build();
    }
}
