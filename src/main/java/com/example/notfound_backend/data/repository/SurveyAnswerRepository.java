package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.dto.SurveyDTO;
import com.example.notfound_backend.data.entity.SurveyAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswerEntity, Integer> {

    List<SurveyAnswerEntity> findByParentId_Id(int parentId);

    List<SurveyAnswerEntity> findByUser_Username(String username);
}
