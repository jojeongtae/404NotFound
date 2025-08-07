package com.example.notfound_backend.data.repository.pointboard;

import com.example.notfound_backend.data.entity.pointboard.QuizResultEntity;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizResultRepository extends JpaRepository<QuizResultEntity,Integer> {

    List<QuizResultEntity> findByQuiz_Id(Integer quizId);

    Optional<QuizResultEntity> findByUsername(UserAuthEntity username);

}
