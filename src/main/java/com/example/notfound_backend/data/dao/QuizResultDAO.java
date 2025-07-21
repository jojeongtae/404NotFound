package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.dto.QuizResultDTO;
import com.example.notfound_backend.data.entity.QuizResultEntity;
import com.example.notfound_backend.data.repository.QuizResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuizResultDAO {

    private final QuizResultRepository quizResultRepository;

    public QuizResultEntity save(QuizResultEntity entity) {
        return quizResultRepository.save(entity);
    }

    public List<QuizResultEntity> findByQuizId(int quizId) {
        return  quizResultRepository.findByQuiz_Id(quizId);
    }
}
