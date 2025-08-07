package com.example.notfound_backend.data.repository.normalboard.comment;

import com.example.notfound_backend.data.entity.normalboard.comment.BoardNoticeCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardNoticeCommentRepository extends JpaRepository<BoardNoticeCommentEntity, Integer> {
    List<BoardNoticeCommentEntity> findAllByBoardIdOrderByCreatedAtDesc(Integer boardId);
}
