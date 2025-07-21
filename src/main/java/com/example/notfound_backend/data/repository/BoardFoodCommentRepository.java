package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.BoardFoodCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardFoodCommentRepository extends JpaRepository<BoardFoodCommentEntity, Integer> {
    List<BoardFoodCommentEntity> findAllByBoardIdOrderByCreatedAtDesc(Integer boardId);
}
