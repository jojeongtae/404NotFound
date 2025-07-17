package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.BoardFreeDTO;
import com.example.notfound_backend.service.BoardFreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardFreeController {
    private final BoardFreeService boardFreeService;

    @GetMapping("/list")
    public List<BoardFreeDTO> getAllBoards() {
        List<BoardFreeDTO> boardFreeDtoList = boardFreeService.findAll();
        System.out.println(boardFreeDtoList.size());
        return boardFreeDtoList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardFreeDTO> getBoard(@PathVariable Integer id){
        BoardFreeDTO dto= boardFreeService.viewBoard(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<BoardFreeDTO> create(@RequestBody BoardFreeDTO boardFreeDTO) {
        BoardFreeDTO created = boardFreeService.createBoard(boardFreeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardFreeDTO> update(@PathVariable Integer id, @RequestBody BoardFreeDTO boardFreeDTO) {
        BoardFreeDTO updated = boardFreeService.updateBoard(id, boardFreeDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boardFreeService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/recommend")
    public ResponseEntity<BoardFreeDTO> recommend(@PathVariable Integer id) {
        BoardFreeDTO updated= boardFreeService.recommendBoard(id);
        return ResponseEntity.ok(updated);
    }
}
