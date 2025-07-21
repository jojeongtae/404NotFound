package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.service.BoardFreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/free")
public class BoardFreeController {
    private final BoardFreeService boardFreeService;

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
    public ResponseEntity<BoardDTO> create(@RequestBody BoardDTO boardDTO) {
        BoardDTO created = boardFreeService.createBoard(boardDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDTO> update(@PathVariable Integer id, @RequestBody BoardDTO boardDTO) {
        BoardDTO updated = boardFreeService.updateBoard(id, boardDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boardFreeService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/recommend")
    public ResponseEntity<BoardDTO> recommend(@PathVariable Integer id) {
        BoardDTO updated= boardFreeService.recommendBoard(id);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/cancel_recommend")
    public ResponseEntity<BoardDTO> cancelRecommend(@PathVariable Integer id) {
        BoardDTO updated= boardFreeService.cancelRecommendBoard(id);
        return ResponseEntity.ok(updated);
    }

}
