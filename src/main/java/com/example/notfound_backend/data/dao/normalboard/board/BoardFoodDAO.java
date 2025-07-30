package com.example.notfound_backend.data.dao.normalboard.board;

import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
import com.example.notfound_backend.data.entity.enumlist.Status;
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

    public List<BoardFoodEntity> findAllByStatus(Status status) {
        return boardFoodRepository.findAllByStatus(status);
    }

    public List<BoardFoodEntity> findAllBoardsByUser(String username) {
        return boardFoodRepository.findAllByUser(username);
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

    public List<BoardRankingDTO> getTop5ByRecommendInLast7Days() {
        return boardFoodRepository.findTop5ByRecommendInLast7Days();
    }

    public List<BoardRankingDTO> getTop5ByCommentsInLast7Days() {
        return boardFoodRepository.findTop5ByCommentsInLast7Days();
    }

    public List<BoardRankingDTO> getTop5ByRecommend(){
        return boardFoodRepository.findTop5ByRecommend();
    }

    public List<BoardFoodEntity> findByTitle(String title) { return boardFoodRepository.findByTitle(title); }

    public List<BoardFoodEntity> findByAuthor(String author) { return boardFoodRepository.findByAuthor(author); }

}
