package com.example.notfound_backend.service.pointboard;

import com.example.notfound_backend.data.dao.pointboard.SurveyDAO;
import com.example.notfound_backend.data.dao.login.UserAuthDAO;
import com.example.notfound_backend.data.dao.admin.UserInfoDAO;
import com.example.notfound_backend.data.dto.pointboard.SurveyDTO;
import com.example.notfound_backend.data.entity.admin.UserInfoEntity;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.pointboard.SurveyEntity;
import com.example.notfound_backend.service.admin.UserInfoService;
import lombok.RequiredArgsConstructor;
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
    private final UserInfoService userInfoService;

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
            UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(surveyEntity.getAuthor().getUsername());
            String userNickname = userInfoEntity.getNickname();
            String userGrade = userInfoService.getUserGrade(userInfoEntity.getUsername().getUsername());
            surveyDTO.setAuthorNickname(userNickname);
            surveyDTO.setGrade(userGrade);
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
        UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(entity.getAuthor().getUsername());
        String userNickname = userInfoEntity.getNickname();
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
                userInfoService.getUserGrade(userInfoEntity.getUsername().getUsername()),
                entity.getCreatedAt(),
                entity.getCategory(),
                entity.getViews()
        );
    }

    @Transactional
    public SurveyDTO createBoard(SurveyDTO surveyDTO){
        userInfoService.userStatusValidator(surveyDTO.getAuthor());

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
        userInfoService.userStatusValidator(surveyDTO.getAuthor());

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
