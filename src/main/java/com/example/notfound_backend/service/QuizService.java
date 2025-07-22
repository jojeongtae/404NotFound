package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.QuizDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.QuizDTO;
import com.example.notfound_backend.data.entity.QuizEntity;
import com.example.notfound_backend.data.entity.Status;
import com.example.notfound_backend.data.entity.Type;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizDAO quizDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;


    public List<QuizDTO> findAll() {
        List<QuizEntity> quizEntities = quizDAO.findAllBoards();
        List<QuizDTO> quizDTOList =new ArrayList<>();
        for(QuizEntity quizEntity : quizEntities){
            QuizDTO quizDTO =new QuizDTO();
            quizDTO.setId(quizEntity.getId());
            quizDTO.setTitle(quizEntity.getTitle());
            quizDTO.setQuestion(quizEntity.getQuestion());
            quizDTO.setAnswer(quizEntity.getAnswer());

            if (quizEntity.getAuthor() != null) {
                quizDTO.setAuthor(quizEntity.getAuthor().getUsername());
            }
            String userNickname = userInfoDAO.getUserInfo(quizEntity.getAuthor().getUsername()).getNickname();
            quizDTO.setAuthorNickname(userNickname);
            quizDTO.setCreatedAt(quizEntity.getCreatedAt());
            quizDTO.setLevel(quizEntity.getLevel());
            quizDTO.setCategory(quizEntity.getCategory());
            quizDTO.setType(quizEntity.getType().name());
            quizDTO.setViews(quizEntity.getViews());
            quizDTOList.add(quizDTO);
        }
        return quizDTOList;
    }

    @Transactional
    public QuizDTO viewBoard(Integer id) {
        quizDAO.incrementViews(id);
        QuizEntity entity= quizDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Quiz not found"));
        return toDTO(entity);
    }

    private QuizDTO toDTO(QuizEntity entity) {
        String userNickname = userInfoDAO.getUserInfo(entity.getAuthor().getUsername()).getNickname();
        return new QuizDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getQuestion(),
                entity.getAnswer(),
                entity.getAuthor().getUsername(),
                userNickname,
                entity.getCreatedAt(),
                entity.getLevel(),
                entity.getCategory(),
                entity.getType() != null ? entity.getType().name() : null,
                entity.getViews()
        );
    }

    @Transactional
    public QuizDTO createBoard(QuizDTO quizDTO) {
        QuizEntity entity = new QuizEntity();
        entity.setTitle(quizDTO.getTitle());
        entity.setQuestion(quizDTO.getQuestion());
        entity.setAnswer(quizDTO.getAnswer());

        UserAuthEntity author = userAuthDAO.findByUsername(quizDTO.getAuthor());
        entity.setAuthor(author);

        entity.setCreatedAt(Instant.now());
        entity.setLevel(quizDTO.getLevel());
        entity.setCategory("QUIZ");
        entity.setType(Type.valueOf(quizDTO.getType()));
        entity.setViews(0); // 새 글이니 조회수 0으로 시작
        QuizEntity saved = quizDAO.save(entity);
        userInfoDAO.updatePoint(quizDTO.getAuthor(), 3); // 3포인트증가
        return toDTO(saved);
    }

    @Transactional
    public QuizDTO updateBoard(Integer id, QuizDTO quizDTO) {
        QuizEntity entity = quizDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        // 원하는 필드만 수정
        entity.setTitle(quizDTO.getTitle());
        entity.setQuestion(quizDTO.getQuestion());
        entity.setAnswer(quizDTO.getAnswer());
        entity.setLevel(quizDTO.getLevel());
        entity.setType(Type.valueOf(quizDTO.getType()));

        QuizEntity updated = quizDAO.save(entity);
        return toDTO(updated);
    }

    @Transactional
    public void deleteBoard(Integer id) {
        QuizEntity entity = quizDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        quizDAO.delete(entity);
    }
}
