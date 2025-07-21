package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.BoardNoticeCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardNoticeCommentRepository extends JpaRepository<BoardNoticeCommentEntity, Integer> {
    List<BoardNoticeCommentEntity> findAllByBoardIdOrderByCreatedAtDesc(Integer boardId);
}
