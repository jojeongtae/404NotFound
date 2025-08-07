package com.example.notfound_backend.data.repository.pointboard;

import com.example.notfound_backend.data.entity.pointboard.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity,Integer> {

    List<QuizEntity> findAll();

    @Modifying
    @Query("UPDATE QuizEntity q SET q.views=q.views+1 WHERE q.id=:id")
    void incrementViews(@Param("id") Integer id);

    Optional<QuizEntity> findById(Integer id);
}
