package com.example.notfound_backend.data.repository.normalboard.comment;

import com.example.notfound_backend.data.entity.normalboard.comment.BoardQnaCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardQnaCommentRepository extends JpaRepository<BoardQnaCommentEntity, Integer> {
    List<BoardQnaCommentEntity> findAllByBoardIdOrderByCreatedAtDesc(Integer boardId);
}
