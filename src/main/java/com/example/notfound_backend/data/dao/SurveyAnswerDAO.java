package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.SurveyAnswerEntity;
import com.example.notfound_backend.data.repository.SurveyAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SurveyAnswerDAO {

    private final SurveyAnswerRepository surveyAnswerRepository;

    public SurveyAnswerEntity save(SurveyAnswerEntity entity) {
        return surveyAnswerRepository.save(entity);
    }

    public List<SurveyAnswerEntity> findByParentId(Integer parentId) {
        return surveyAnswerRepository.findByParentId_Id(parentId);
    }

    public List<SurveyAnswerEntity> findByUsername(String username) {
        return surveyAnswerRepository.findByUser_Username(username);
    }

}
