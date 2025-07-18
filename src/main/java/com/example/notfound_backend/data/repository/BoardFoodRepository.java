package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.BoardFoodEntity;
import com.example.notfound_backend.data.entity.BoardFreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardFoodRepository extends JpaRepository<BoardFoodEntity, Integer> {

    List<BoardFoodEntity> findAll();

    @Modifying
    @Query("UPDATE BoardFoodEntity b SET b.views=b.views+1 WHERE b.id=:id")
    void incrementViews(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE BoardFoodEntity b SET b.recommend = b.recommend + 1 WHERE b.id = :id")
    void incrementRecommend(@Param("id") Integer id);

    Optional<BoardFoodEntity> findById(Integer id);

}
