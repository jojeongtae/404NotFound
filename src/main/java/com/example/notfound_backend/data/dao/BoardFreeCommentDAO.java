package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.BoardFreeCommentEntity;
import com.example.notfound_backend.data.repository.BoardFreeCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardFreeCommentDAO {

    private final BoardFreeCommentRepository boardFreeCommentRepository;

    public Optional<BoardFreeCommentEntity> findById(Integer id) {
        return boardFreeCommentRepository.findById(id);
    }

    public List<BoardFreeCommentEntity> findAllByBoardIdOrderByCreatedAtDesc(Integer boardId) {
        return boardFreeCommentRepository.findAllByBoardIdOrderByCreatedAtDesc(boardId);
    }

    public BoardFreeCommentEntity save(BoardFreeCommentEntity entity){
        return boardFreeCommentRepository.save(entity);
    }

    public void delete(BoardFreeCommentEntity entity){boardFreeCommentRepository.delete(entity);}


}
