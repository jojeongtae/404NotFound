package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.QuizDAO;
import com.example.notfound_backend.data.dao.QuizResultDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.QuizResultDTO;
import com.example.notfound_backend.data.entity.QuizEntity;
import com.example.notfound_backend.data.entity.QuizResultEntity;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.data.entity.UserStatus;
import com.example.notfound_backend.exception.UserSuspendedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizResultService {
    private final QuizResultDAO quizResultDAO;
    private final QuizDAO quizDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;

    @Transactional
    public QuizResultDTO submitAnswer(QuizResultDTO dto){
        UserStatus userStatus = userInfoDAO.getUserInfo(dto.getUsername()).getStatus();
        if (userStatus != UserStatus.ACTIVE) {
            throw new UserSuspendedException("활동 정지된 사용자입니다.");
        }

        QuizResultEntity entity=new QuizResultEntity();

        if(dto.getUsername()!=null){
            UserAuthEntity user=userAuthDAO.findByUsername(dto.getUsername());
            entity.setUsername(user);
        }

        entity.setUserAnswer(dto.getUserAnswer());
        QuizEntity quiz = quizDAO.findById(dto.getQuiz_id()).orElseThrow(() -> new RuntimeException("Quiz not found"));
        entity.setQuiz(quiz);
        entity.setResult(dto.getResult());

        entity.setSolvedAt(Instant.now());

        if (dto.getResult() == 1) { // 퀴즈 맞추면,
            userInfoDAO.updatePoint(dto.getUsername(), 2); // 3포인트증가
        }

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
                .userNickname(userInfoDAO.getUserInfo(entity.getUsername().getUsername()).getNickname())
                .userAnswer(entity.getUserAnswer())
                .result(entity.getResult())
                .solvedAt(entity.getSolvedAt())
                .build();
    }

    public QuizResultDTO findByUsername(UserAuthEntity user){
        QuizResultEntity result = quizResultDAO.findByUsername(user)
                .orElseThrow(() -> new RuntimeException("해당 사용자의 퀴즈 결과가 없습니다."));
        return toDTO(result);
    }
}
