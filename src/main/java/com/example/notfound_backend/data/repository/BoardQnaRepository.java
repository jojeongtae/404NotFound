package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.BoardFreeEntity;
import com.example.notfound_backend.data.entity.BoardQnaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardQnaRepository extends JpaRepository<BoardQnaEntity, Integer> {

    List<BoardQnaEntity> findAll();

    @Modifying
    @Query("UPDATE BoardQnaEntity b SET b.views=b.views+1 WHERE b.id=:id")
    void incrementViews(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE BoardQnaEntity b SET b.recommend = b.recommend + 1 WHERE b.id = :id")
    void incrementRecommend(@Param("id") Integer id);

    Optional<BoardQnaEntity> findById(Integer id);

}
