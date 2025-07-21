package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.QuizResultDTO;
import com.example.notfound_backend.service.QuizResultService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz-answers")
public class QuizResultController {

    private final QuizResultService quizResultService;

    @PostMapping("/new")
    public ResponseEntity<QuizResultDTO> submitResult(@RequestBody QuizResultDTO dto){
        return ResponseEntity.ok(quizResultService.submitAnswer(dto));
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<List<QuizResultDTO>> getResultByQuiz(@PathVariable int quizId){
        return ResponseEntity.ok(quizResultService.getResultByQuiz(quizId));
    }
}
