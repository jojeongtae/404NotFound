package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {

    List<BoardEntity> findAll();

    @Modifying
    @Query("UPDATE BoardEntity b SET b.views=b.views+1 WHERE b.id=:id")
    void incrementViews(@Param("id") Integer id);

    Optional<BoardEntity> findById(Integer id);
}
