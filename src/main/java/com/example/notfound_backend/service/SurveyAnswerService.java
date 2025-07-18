package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.SurveyAnswerDAO;
import com.example.notfound_backend.data.dao.SurveyDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dto.SurveyAnswerDTO;
import com.example.notfound_backend.data.entity.SurveyAnswerEntity;
import com.example.notfound_backend.data.entity.SurveyEntity;
import com.example.notfound_backend.data.entity.UserAuthEntity;
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

    @Transactional
    public SurveyAnswerDTO submitAnswer(SurveyAnswerDTO dto) {
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
        return toDTO(saved);
    }

    public List<SurveyAnswerDTO> getAnswersBySurvey(int parentId) {
        return surveyAnswerDAO.findByParentId(parentId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private SurveyAnswerDTO toDTO(SurveyAnswerEntity entity) {
        return SurveyAnswerDTO.builder()
                .id(entity.getId())
                .username(entity.getUser() != null ? entity.getUser().getUsername() : null)
                .answers(entity.getAnswers())
                .createdAt(entity.getCreatedAt())
                .parentId(entity.getParentId().getId())
                .build();
    }
}
