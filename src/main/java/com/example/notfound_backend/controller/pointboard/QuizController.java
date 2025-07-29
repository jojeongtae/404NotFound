package com.example.notfound_backend.controller.pointboard;

import com.example.notfound_backend.data.dto.pointboard.QuizDTO;
import com.example.notfound_backend.service.pointboard.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<QuizDTO> create(@RequestPart("quizDTO") QuizDTO quizDTO,
                                          @RequestPart(value = "file",  required = false) MultipartFile file) throws IOException {
        QuizDTO created = quizService.createBoard(quizDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizDTO> update(@PathVariable Integer id,
                                          @RequestPart("quizDTO") QuizDTO quizDTO,
                                          @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        QuizDTO updated = quizService.updateBoard(id, quizDTO, file);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws IOException {
        quizService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }
}
