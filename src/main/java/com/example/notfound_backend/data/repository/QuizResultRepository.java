package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.QuizResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResultEntity,Integer> {

    List<QuizResultEntity> findByQuiz_Id(Integer quizId);
}
