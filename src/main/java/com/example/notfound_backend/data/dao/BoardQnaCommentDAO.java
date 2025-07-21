package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.BoardQnaCommentEntity;
import com.example.notfound_backend.data.repository.BoardQnaCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardQnaCommentDAO {

    private final BoardQnaCommentRepository boardQnaCommentRepository;

    public Optional<BoardQnaCommentEntity> findById(Integer id) {
        return boardQnaCommentRepository.findById(id);
    }

    public List<BoardQnaCommentEntity> findAllByBoardIdOrderByCreatedAtDesc(Integer boardId) {
        return boardQnaCommentRepository.findAllByBoardIdOrderByCreatedAtDesc(boardId);
    }

    public BoardQnaCommentEntity save(BoardQnaCommentEntity entity){
        return boardQnaCommentRepository.save(entity);
    }

    public void delete(BoardQnaCommentEntity entity){boardQnaCommentRepository.delete(entity);}

}
