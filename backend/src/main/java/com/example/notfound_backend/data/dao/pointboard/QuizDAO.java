package com.example.notfound_backend.data.dao.pointboard;

import com.example.notfound_backend.data.entity.pointboard.QuizEntity;
import com.example.notfound_backend.data.repository.pointboard.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizDAO {
    private final QuizRepository quizRepository;

    public List<QuizEntity> findAllBoards() {
        return quizRepository.findAll();
    }

    public QuizEntity save(QuizEntity quizEntity) {
        return quizRepository.save(quizEntity);
    }

    @Transactional
    public void incrementViews(Integer id) {
        quizRepository.incrementViews(id);
    }

    public Optional<QuizEntity> findById(Integer id) {
        return quizRepository.findById(id);
    }

    public void delete(QuizEntity quizEntity) { quizRepository.delete(quizEntity); }

}
