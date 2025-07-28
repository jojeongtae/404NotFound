package com.example.notfound_backend.data.dao.normalboard.board;

import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
import com.example.notfound_backend.data.entity.normalboard.board.BoardFoodEntity;
import com.example.notfound_backend.data.repository.normalboard.board.BoardFoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardFoodDAO {

    private final BoardFoodRepository boardFoodRepository;

    public List<BoardFoodEntity> findAllBoards() {
        return boardFoodRepository.findAll();
    }

    public BoardFoodEntity save(BoardFoodEntity boardFoodEntity) {
        return boardFoodRepository.save(boardFoodEntity);
    }

//    @Transactional
//    public void incrementRecommend(Integer id) {
//        boardFoodRepository.incrementRecommend(id);
//    }
//
//    @Transactional
//    public void decrementRecommend(Integer id) {boardFoodRepository.decrementRecommend(id);}

    @Transactional
    public void incrementViews(Integer id) {
        boardFoodRepository.incrementViews(id);
    }

    public Optional<BoardFoodEntity> findById(Integer id) {
        return boardFoodRepository.findById(id);
    }

    public void delete(BoardFoodEntity boardFoodEntity) {
        boardFoodRepository.delete(boardFoodEntity);
    }

    public List<BoardRankingDTO> getTop5ByRecommendToday() {
        return boardFoodRepository.findTop5ByRecommendToday();
    }

    public List<BoardRankingDTO> getTop5ByCommentsToday() {
        return boardFoodRepository.findTop5ByCommentsToday();
    }

    public List<BoardFoodEntity> findByTitle(String title) { return boardFoodRepository.findByTitle(title); }

    public List<BoardFoodEntity> findByAuthor(String author) { return boardFoodRepository.findByAuthor(author); }

}
