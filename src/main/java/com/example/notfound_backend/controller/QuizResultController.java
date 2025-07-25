package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dto.QuizResultDTO;
import com.example.notfound_backend.data.entity.UserAuthEntity;
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
    private final UserAuthDAO userAuthDAO;

    @PostMapping("/new")
    public ResponseEntity<QuizResultDTO> submitResult(@RequestBody QuizResultDTO dto){
        return ResponseEntity.ok(quizResultService.submitAnswer(dto));
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuizResultDTO>> getResultByQuiz(@PathVariable int quizId){
        return ResponseEntity.ok(quizResultService.getResultByQuiz(quizId));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<QuizResultDTO> getResultByUsername(@PathVariable String username) {
        UserAuthEntity user = userAuthDAO.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("해당 사용자를 찾을 수 없습니다.");
        }

        QuizResultDTO result = quizResultService.findByUsername(user);
        return ResponseEntity.ok(result);
    }

}
