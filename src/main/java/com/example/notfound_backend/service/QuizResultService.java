package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.QuizDAO;
import com.example.notfound_backend.data.dao.QuizResultDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dto.QuizResultDTO;
import com.example.notfound_backend.data.entity.QuizEntity;
import com.example.notfound_backend.data.entity.QuizResultEntity;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizResultService {
    private final QuizResultDAO quizResultDAO;
    private final QuizDAO quizDAO;
    private final UserAuthDAO userAuthDAO;

    @Transactional
    public QuizResultDTO submitAnswer(QuizResultDTO dto){
        QuizResultEntity entity=new QuizResultEntity();

        if(dto.getUsername()!=null){
            UserAuthEntity user=userAuthDAO.findByUsername(dto.getUsername());
            entity.setUsername(user);
        }

        entity.setUserAnswer(dto.getUserAnswer());
        QuizEntity quiz = quizDAO.findById(dto.getQuiz_id()).orElseThrow(() -> new RuntimeException("Quiz not found"));
        entity.setQuiz(quiz);

        entity.setTimeTaken(dto.getTimeTaken()); // 초 단위 시간 (프론트 or 컨트롤러에서 계산해서 넣어줘야 함)
        entity.setSolvedAt(Instant.now());

        // 저장
        QuizResultEntity saved = quizResultDAO.save(entity);
        return toDTO(saved);
    }

    public List<QuizResultDTO> getResultByQuiz(int quizId){
        return quizResultDAO.findByQuizId(quizId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private QuizResultDTO toDTO(QuizResultEntity entity){
        return QuizResultDTO.builder()
                .id(entity.getId())
                .quiz_id(entity.getQuiz().getId())
                .username(entity.getUsername().getUsername())
                .userAnswer(entity.getUserAnswer())
                .result(entity.getResult())
                .timeTaken(entity.getTimeTaken())
                .solvedAt(entity.getSolvedAt())
                .build();
    }
}
