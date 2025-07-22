package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.dto.BoardRankingDTO;
import com.example.notfound_backend.data.entity.BoardFreeEntity;
import com.example.notfound_backend.data.repository.BoardFreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardFreeDAO {
    private final BoardFreeRepository boardFreeRepository;

    public List<BoardFreeEntity> findAllBoards() {
        return boardFreeRepository.findAll();
    }

    public BoardFreeEntity save(BoardFreeEntity boardFreeEntity) {
        return boardFreeRepository.save(boardFreeEntity);
    }

//    @Transactional
//    public void incrementRecommend(Integer id) {
//        boardFreeRepository.incrementRecommend(id);
//    }
//
//    @Transactional
//    public void decrementRecommend(Integer id) {boardFreeRepository.decrementRecommend(id);}

    @Transactional
    public void incrementViews(Integer id) {
        boardFreeRepository.incrementViews(id);
    }

    public Optional<BoardFreeEntity> findById(Integer id) {
        return boardFreeRepository.findById(id);
    }

    public void delete(BoardFreeEntity boardFreeEntity) {
        boardFreeRepository.delete(boardFreeEntity);
    }

    public List<BoardRankingDTO> getTop5ByRecommendToday() {
        return boardFreeRepository.findTop5ByRecommendToday();
    }

    public List<BoardRankingDTO> getTop5ByCommentsToday() {
        return boardFreeRepository.findTop5ByCommentsToday();
    }

}

