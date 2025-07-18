package com.example.notfound_backend.controller;


import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.service.BoardFoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food")
public class BoardFoodController {
    private final BoardFoodService boardFoodService;

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

    @PatchMapping("/{id}/recommend")
    public ResponseEntity<BoardDTO> recommend(@PathVariable Integer id) {
        BoardDTO updated= boardFoodService.recommendBoard(id);
        return ResponseEntity.ok(updated);
    }
}