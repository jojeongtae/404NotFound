package com.example.notfound_backend.service.pointboard;

import com.example.notfound_backend.data.dao.pointboard.SurveyAnswerDAO;
import com.example.notfound_backend.data.dao.pointboard.SurveyDAO;
import com.example.notfound_backend.data.dao.login.UserAuthDAO;
import com.example.notfound_backend.data.dao.admin.UserInfoDAO;
import com.example.notfound_backend.data.dto.pointboard.SurveyAnswerDTO;
import com.example.notfound_backend.data.entity.admin.UserInfoEntity;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.pointboard.SurveyAnswerEntity;
import com.example.notfound_backend.data.entity.pointboard.SurveyEntity;
import com.example.notfound_backend.service.admin.UserInfoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyAnswerService {

    private final SurveyAnswerDAO surveyAnswerDAO;
    private final SurveyDAO surveyDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;
    private final UserInfoService userInfoService;

    @Transactional
    public SurveyAnswerDTO submitAnswer(SurveyAnswerDTO dto) {
        userInfoService.userStatusValidator(dto.getUsername());

        SurveyAnswerEntity entity = new SurveyAnswerEntity();

        if (dto.getUsername() != null) {
            UserAuthEntity user = userAuthDAO.findByUsername(dto.getUsername());
            entity.setUser(user);
        }

        entity.setAnswers(dto.getAnswers());
        entity.setCreatedAt(Instant.now());

        SurveyEntity parentSurvey=surveyDAO.findById(dto.getParentId()).get();
        entity.setParentId(parentSurvey);

        SurveyAnswerEntity saved = surveyAnswerDAO.save(entity);
        userInfoDAO.updatePoint(dto.getUsername(), 1); // 1포인트증가
        return toDTO(saved);
    }

    public List<SurveyAnswerDTO> getAnswersBySurvey(int parentId) {
        return surveyAnswerDAO.findByParentId(parentId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private SurveyAnswerDTO toDTO(SurveyAnswerEntity entity) {
        UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(entity.getUser().getUsername());
        return SurveyAnswerDTO.builder()
                .id(entity.getId())
                .username(entity.getUser() != null ? entity.getUser().getUsername() : null)
                .userNickname(userInfoEntity.getNickname())
                .answers(entity.getAnswers())
                .createdAt(entity.getCreatedAt())
                .parentId(entity.getParentId().getId())
                .grade(userInfoService.getUserGrade(userInfoEntity.getUsername().getUsername()))
                .build();
    }
}
