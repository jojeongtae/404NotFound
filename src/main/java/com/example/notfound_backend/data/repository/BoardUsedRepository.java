package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.BoardFreeEntity;
import com.example.notfound_backend.data.entity.BoardUsedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardUsedRepository extends JpaRepository<BoardUsedEntity, Integer> {

    List<BoardUsedEntity> findAll();

    @Modifying
    @Query("UPDATE BoardUsedEntity b SET b.views=b.views+1 WHERE b.id=:id")
    void incrementViews(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE BoardUsedEntity b SET b.recommend = b.recommend + 1 WHERE b.id = :id")
    void incrementRecommend(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE BoardUsedEntity b SET b.recommend = b.recommend - 1 WHERE b.id = :id AND b.recommend > 0")
    void decrementRecommend(@Param("id") Integer id);

    Optional<BoardUsedEntity> findById(Integer id);

}
