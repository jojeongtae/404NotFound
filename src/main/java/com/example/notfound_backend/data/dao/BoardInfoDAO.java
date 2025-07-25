package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.dto.BoardRankingDTO;
import com.example.notfound_backend.data.entity.BoardInfoEntity;
import com.example.notfound_backend.data.repository.BoardInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardInfoDAO {

    private final BoardInfoRepository boardInfoRepository;

    public List<BoardInfoEntity> findAllBoards() {
        return boardInfoRepository.findAll();
    }

    public BoardInfoEntity save(BoardInfoEntity boardInfoEntity) {
        return boardInfoRepository.save(boardInfoEntity);
    }

//    @Transactional
//    public void incrementRecommend(Integer id) {
//        boardInfoRepository.incrementRecommend(id);
//    }
//
//    @Transactional
//    public void decrementRecommend(Integer id) {boardInfoRepository.decrementRecommend(id);}

    @Transactional
    public void incrementViews(Integer id) {
        boardInfoRepository.incrementViews(id);
    }

    public Optional<BoardInfoEntity> findById(Integer id) {
        return boardInfoRepository.findById(id);
    }

    public void delete(BoardInfoEntity boardInfoEntity) {
        boardInfoRepository.delete(boardInfoEntity);
    }

    public List<BoardRankingDTO> getTop5ByRecommendToday() {
        return boardInfoRepository.findTop5ByRecommendToday();
    }

    public List<BoardRankingDTO> getTop5ByCommentsToday() {
        return boardInfoRepository.findTop5ByCommentsToday();
    }

    public List<BoardInfoEntity> findByTitle(String title){ return boardInfoRepository.findByTitle(title); }

    public List<BoardInfoEntity> findByAuthor(String author){ return boardInfoRepository.findByAuthor(author); }

}
