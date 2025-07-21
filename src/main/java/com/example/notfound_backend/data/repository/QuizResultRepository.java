package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.QuizResultEntity;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizResultRepository extends JpaRepository<QuizResultEntity,Integer> {

    List<QuizResultEntity> findByQuiz_Id(Integer quizId);

    Optional<QuizResultEntity> findByUsername(UserAuthEntity username);

}
