package com.example.notfound_backend.data.dao.normalboard.comment;

import com.example.notfound_backend.data.entity.normalboard.comment.BoardUsedCommentEntity;
import com.example.notfound_backend.data.repository.normalboard.comment.BoardUsedCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardUsedCommentDAO {

    private final BoardUsedCommentRepository boardUsedCommentRepository;

    public Optional<BoardUsedCommentEntity> findById(Integer id) {
        return boardUsedCommentRepository.findById(id);
    }

    public List<BoardUsedCommentEntity> findAllByBoardIdOrderByCreatedAtDesc(Integer boardId) {
        return boardUsedCommentRepository.findAllByBoardIdOrderByCreatedAtDesc(boardId);
    }

    public BoardUsedCommentEntity save(BoardUsedCommentEntity entity){
        return boardUsedCommentRepository.save(entity);
    }

    public void delete(BoardUsedCommentEntity entity){boardUsedCommentRepository.delete(entity);}


}
