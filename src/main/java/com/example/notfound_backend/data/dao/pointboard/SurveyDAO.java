package com.example.notfound_backend.data.dao.pointboard;

import com.example.notfound_backend.data.entity.pointboard.SurveyEntity;
import com.example.notfound_backend.data.repository.pointboard.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurveyDAO {
    private final SurveyRepository surveyRepository;

    public List<SurveyEntity> findAllBoards() {
        return surveyRepository.findAll();
    }

    public SurveyEntity save(SurveyEntity surveyEntity) {
        return surveyRepository.save(surveyEntity);
    }

    @Transactional
    public void incrementViews(Integer id) {
        surveyRepository.incrementViews(id);
    }

    public Optional<SurveyEntity> findById(Integer id) {
        return surveyRepository.findById(id);
    }

    public void delete(SurveyEntity surveyEntity) { surveyRepository.delete(surveyEntity); }

}
