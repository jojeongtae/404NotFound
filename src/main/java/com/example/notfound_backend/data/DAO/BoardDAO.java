package com.example.notfound_backend.data.DAO;

import com.example.notfound_backend.data.entity.BoardEntity;
import com.example.notfound_backend.data.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardDAO {
    private BoardRepository boardRepository;

    public List<BoardEntity> findAllBoards() {
        return boardRepository.findAll();
    }

    public BoardEntity save(BoardEntity boardEntity) {
        return boardRepository.save(boardEntity);
    }

    @Transactional
    public void incrementViews(Integer id) {
        boardRepository.incrementViews(id);
    }

    public Optional<BoardEntity> findById(Integer id) {
        return boardRepository.findById(id);
    }

}

