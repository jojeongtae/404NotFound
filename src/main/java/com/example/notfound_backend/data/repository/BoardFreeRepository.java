package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.BoardFreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardFreeRepository extends JpaRepository<BoardFreeEntity, Integer> {

    List<BoardFreeEntity> findAll();

    @Modifying
    @Query("UPDATE BoardFreeEntity b SET b.views=b.views+1 WHERE b.id=:id")
    void incrementViews(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE BoardFreeEntity b SET b.recommend = b.recommend + 1 WHERE b.id = :id")
    void incrementRecommend(@Param("id") Integer id);

    Optional<BoardFreeEntity> findById(Integer id);
}
