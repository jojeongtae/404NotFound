package com.example.notfound_backend.data.repository.pointboard;

import com.example.notfound_backend.data.entity.pointboard.SurveyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<SurveyEntity, Integer> {

    List<SurveyEntity> findAll();

    @Modifying
    @Query("UPDATE SurveyEntity s SET s.views=s.views+1 WHERE s.id=:id")
    void incrementViews(@Param("id") Integer id);

    Optional<SurveyEntity> findById(Integer id);

}
