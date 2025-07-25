package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.BoardUsedCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardUsedCommentRepository extends JpaRepository<BoardUsedCommentEntity, Integer> {
    List<BoardUsedCommentEntity> findAllByBoardIdOrderByCreatedAtDesc(Integer boardId);
}
