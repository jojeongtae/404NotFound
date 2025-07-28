package com.example.notfound_backend.data.dao.normalboard.board;

import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
import com.example.notfound_backend.data.entity.normalboard.board.BoardUsedEntity;
import com.example.notfound_backend.data.repository.normalboard.board.BoardUsedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardUsedDAO {

    private final BoardUsedRepository boardUsedRepository;

    public List<BoardUsedEntity> findAllBoards() {
        return boardUsedRepository.findAll();
    }

    public BoardUsedEntity save(BoardUsedEntity boardUsedEntity) {
        return boardUsedRepository.save(boardUsedEntity);
    }

//    @Transactional
//    public void incrementRecommend(Integer id) {
//        boardUsedRepository.incrementRecommend(id);
//    }
//
//    @Transactional
//    public void decrementRecommend(Integer id) {boardUsedRepository.decrementRecommend(id);}

    @Transactional
    public void incrementViews(Integer id) {
        boardUsedRepository.incrementViews(id);
    }

    public Optional<BoardUsedEntity> findById(Integer id) {
        return boardUsedRepository.findById(id);
    }

    public void delete(BoardUsedEntity boardUsedEntity) {
        boardUsedRepository.delete(boardUsedEntity);
    }

    public List<BoardRankingDTO> getTop5ByRecommendToday() {
        return boardUsedRepository.findTop5ByRecommendToday();
    }

    public List<BoardRankingDTO> getTop5ByCommentsToday() {
        return boardUsedRepository.findTop5ByCommentsToday();
    }

    public List<BoardUsedEntity> findByTitle(String title) { return boardUsedRepository.findByTitle(title); }

    public List<BoardUsedEntity> findByAuthor(String author) { return boardUsedRepository.findByAuthor(author); }
}
