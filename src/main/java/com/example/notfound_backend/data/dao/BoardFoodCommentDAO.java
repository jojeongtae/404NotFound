package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.BoardFoodCommentEntity;
import com.example.notfound_backend.data.repository.BoardFoodCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardFoodCommentDAO {

    private final BoardFoodCommentRepository boardFoodCommentRepository;

    public Optional<BoardFoodCommentEntity> findById(Integer id) {
        return boardFoodCommentRepository.findById(id);
    }

    public List<BoardFoodCommentEntity> findAllByBoardIdOrderByCreatedAtDesc(Integer boardId) {
        return boardFoodCommentRepository.findAllByBoardIdOrderByCreatedAtDesc(boardId);
    }

    public BoardFoodCommentEntity save(BoardFoodCommentEntity entity){
        return boardFoodCommentRepository.save(entity);
    }

    public void delete(BoardFoodCommentEntity entity){boardFoodCommentRepository.delete(entity);}
}
