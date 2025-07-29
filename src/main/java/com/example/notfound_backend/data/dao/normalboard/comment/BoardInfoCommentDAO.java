package com.example.notfound_backend.data.dao.normalboard.comment;

import com.example.notfound_backend.data.entity.normalboard.comment.BoardInfoCommentEntity;
import com.example.notfound_backend.data.repository.normalboard.comment.BoardInfoCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardInfoCommentDAO {

    private final BoardInfoCommentRepository boardInfoCommentRepository;

    public Optional<BoardInfoCommentEntity> findById(Integer id) {
        return boardInfoCommentRepository.findById(id);
    }

    public List<BoardInfoCommentEntity> findAllByBoardIdOrderByCreatedAtDesc(Integer boardId) {
        return boardInfoCommentRepository.findAllByBoardIdOrderByCreatedAtDesc(boardId);
    }

    public BoardInfoCommentEntity save(BoardInfoCommentEntity entity) {
        return boardInfoCommentRepository.save(entity);
    }

    public void delete(BoardInfoCommentEntity entity) {
        boardInfoCommentRepository.delete(entity);
    }
}
