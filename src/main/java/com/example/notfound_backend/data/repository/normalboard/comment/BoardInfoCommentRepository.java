package com.example.notfound_backend.data.repository.normalboard.comment;

import com.example.notfound_backend.data.entity.normalboard.comment.BoardInfoCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardInfoCommentRepository extends JpaRepository<BoardInfoCommentEntity,Integer> {
    List<BoardInfoCommentEntity> findAllByBoardIdOrderByCreatedAtDesc(Integer boardId);
}
