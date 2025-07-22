package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.SurveyDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.QuizDTO;
import com.example.notfound_backend.data.dto.SurveyDTO;
import com.example.notfound_backend.data.entity.*;
import com.example.notfound_backend.exception.UserSuspendedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyDAO surveyDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;

    public List<SurveyDTO> findAll() {
        List<SurveyEntity> surveyEntities=surveyDAO.findAllBoards();
        List<SurveyDTO> surveyDTOList=new ArrayList<>();
        for(SurveyEntity surveyEntity:surveyEntities){
            SurveyDTO surveyDTO=new SurveyDTO();
            surveyDTO.setId(surveyEntity.getId());
            surveyDTO.setTitle(surveyEntity.getTitle());
            surveyDTO.setQuestion(surveyEntity.getQuestion());
            surveyDTO.setColumn1(surveyEntity.getColumn1());
            surveyDTO.setColumn2(surveyEntity.getColumn2());
            surveyDTO.setColumn3(surveyEntity.getColumn3());
            surveyDTO.setColumn4(surveyEntity.getColumn4());
            surveyDTO.setColumn5(surveyEntity.getColumn5());
            surveyDTO.setAuthor(surveyEntity.getAuthor().getUsername());
            String userNickname = userInfoDAO.getUserInfo(surveyEntity.getAuthor().getUsername()).getNickname();
            surveyDTO.setAuthorNickname(userNickname);
            surveyDTO.setCreatedAt(surveyEntity.getCreatedAt());
            surveyDTO.setCategory(surveyEntity.getCategory());
            surveyDTO.setViews(surveyEntity.getViews());
            surveyDTOList.add(surveyDTO);
        }
        return surveyDTOList;
    }

    @Transactional
    public SurveyDTO viewBoard(Integer id){
        surveyDAO.incrementViews(id);
        SurveyEntity entity=surveyDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Survey Not Found"));
        return toDTO(entity);
    }

    private SurveyDTO toDTO(SurveyEntity entity){
        String userNickname = userInfoDAO.getUserInfo(entity.getAuthor().getUsername()).getNickname();
        return new SurveyDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getQuestion(),
                entity.getColumn1(),
                entity.getColumn2(),
                entity.getColumn3(),
                entity.getColumn4(),
                entity.getColumn5(),
                entity.getAuthor().getUsername(),
                userNickname,
                entity.getCreatedAt(),
                entity.getCategory(),
                entity.getViews()
        );
    }

    @Transactional
    public SurveyDTO createBoard(SurveyDTO surveyDTO){
        UserStatus userStatus = userInfoDAO.getUserInfo(surveyDTO.getAuthor()).getStatus();
        if (userStatus != UserStatus.ACTIVE) {
            throw new UserSuspendedException("활동 정지된 사용자입니다.");
        }
        SurveyEntity entity=new SurveyEntity();
        entity.setTitle(surveyDTO.getTitle());
        entity.setQuestion(surveyDTO.getQuestion());
        entity.setColumn1(surveyDTO.getColumn1());
        entity.setColumn2(surveyDTO.getColumn2());
        entity.setColumn3(surveyDTO.getColumn3());
        entity.setColumn4(surveyDTO.getColumn4());
        entity.setColumn5(surveyDTO.getColumn5());

        UserAuthEntity author=userAuthDAO.findByUsername(surveyDTO.getAuthor());
        entity.setAuthor(author);

        entity.setCreatedAt(Instant.now());
        entity.setCategory("SURVEY");
        entity.setViews(0);
        SurveyEntity saved=surveyDAO.save(entity);
        userInfoDAO.updatePoint(surveyDTO.getAuthor(), 1); // 1포인트증가
        return toDTO(saved);
    }

    @Transactional
    public SurveyDTO updateBoard(Integer id, SurveyDTO surveyDTO) {
        UserStatus userStatus = userInfoDAO.getUserInfo(surveyDTO.getAuthor()).getStatus();
        if (userStatus != UserStatus.ACTIVE) {
            throw new UserSuspendedException("활동 정지된 사용자입니다.");
        }
        SurveyEntity entity = surveyDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        // 원하는 필드만 수정
        entity.setTitle(surveyDTO.getTitle());
        entity.setQuestion(surveyDTO.getQuestion());
        entity.setColumn1(surveyDTO.getColumn1());
        entity.setColumn2(surveyDTO.getColumn2());
        entity.setColumn3(surveyDTO.getColumn3());
        entity.setColumn4(surveyDTO.getColumn4());
        entity.setColumn5(surveyDTO.getColumn5());

        SurveyEntity updated = surveyDAO.save(entity);
        return toDTO(updated);
    }

    @Transactional
    public void deleteBoard(Integer id) {
        SurveyEntity entity = surveyDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        surveyDAO.delete(entity);
    }
}
