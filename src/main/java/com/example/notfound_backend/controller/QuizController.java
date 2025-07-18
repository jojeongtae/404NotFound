package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.QuizDTO;
import com.example.notfound_backend.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/list")
    public List<QuizDTO> getAllBoards() {
        List<QuizDTO> quizDtoList = quizService.findAll();
        System.out.println(quizDtoList.size());
        return quizDtoList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> getBoard(@PathVariable Integer id){
        QuizDTO dto= quizService.viewBoard(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<QuizDTO> create(@RequestBody QuizDTO quizDTO) {
        QuizDTO created = quizService.createBoard(quizDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizDTO> update(@PathVariable Integer id, @RequestBody QuizDTO quizDTO) {
        QuizDTO updated = quizService.updateBoard(id, quizDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        quizService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }
}
