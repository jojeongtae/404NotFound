package com.example.notfound_backend.data.repository.pointboard;

import com.example.notfound_backend.data.entity.pointboard.SurveyAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswerEntity, Integer> {

    List<SurveyAnswerEntity> findByParentId_Id(int parentId);

    List<SurveyAnswerEntity> findByUser_Username(String username);
}
