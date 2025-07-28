package com.example.notfound_backend.controller.pointboard;

import com.example.notfound_backend.data.dao.login.UserAuthDAO;
import com.example.notfound_backend.data.dto.pointboard.QuizResultDTO;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.service.pointboard.QuizResultService;
import lombok.RequiredArgsConstructor;
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
