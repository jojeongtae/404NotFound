package com.example.notfound_backend.data.dao;


import com.example.notfound_backend.data.dto.BoardRankingDTO;
import com.example.notfound_backend.data.entity.BoardQnaEntity;
import com.example.notfound_backend.data.repository.BoardQnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardQnaDAO {

    private final BoardQnaRepository boardQnaRepository;

    public List<BoardQnaEntity> findAllBoards() {
        return boardQnaRepository.findAll();
    }

    public BoardQnaEntity save(BoardQnaEntity boardQnaEntity) {
        return boardQnaRepository.save(boardQnaEntity);
    }

//    @Transactional
//    public void incrementRecommend(Integer id) {
//        boardQnaRepository.incrementRecommend(id);
//    }
//
//    @Transactional
//    public void decrementRecommend(Integer id) {boardQnaRepository.decrementRecommend(id);}

    @Transactional
    public void incrementViews(Integer id) {
        boardQnaRepository.incrementViews(id);
    }

    public Optional<BoardQnaEntity> findById(Integer id) {
        return boardQnaRepository.findById(id);
    }

    public void delete(BoardQnaEntity boardQnaEntity) {
        boardQnaRepository.delete(boardQnaEntity);
    }

    public List<BoardRankingDTO> getTop5ByRecommendToday() {
        return boardQnaRepository.findTop5ByRecommendToday();
    }

    public List<BoardRankingDTO> getTop5ByCommentsToday() {
        return boardQnaRepository.findTop5ByCommentsToday();
    }
}
