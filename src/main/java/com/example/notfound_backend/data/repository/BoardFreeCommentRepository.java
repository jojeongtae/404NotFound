package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.BoardFreeCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardFreeCommentRepository extends JpaRepository<BoardFreeCommentEntity, Integer> {
    List<BoardFreeCommentEntity> findAllByBoardIdOrderByCreatedAtDesc(Integer boardId);
}
