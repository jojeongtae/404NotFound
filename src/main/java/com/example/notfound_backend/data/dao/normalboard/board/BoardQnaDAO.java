package com.example.notfound_backend.data.dao.normalboard.board;


import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
import com.example.notfound_backend.data.entity.enumlist.Status;
import com.example.notfound_backend.data.entity.normalboard.board.BoardQnaEntity;
import com.example.notfound_backend.data.repository.normalboard.board.BoardQnaRepository;
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

    public List<BoardQnaEntity> findAllByStatus(Status status) {
        return boardQnaRepository.findAllByStatus(status);
    }
    public List<BoardQnaEntity> findAllBoardsByUser(String username) {
        return boardQnaRepository.findAllByUser(username);
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

    public List<BoardRankingDTO> getTop5ByRecommendInLast7Days() {
        return boardQnaRepository.findTop5ByRecommendInLast7Days();
    }

    public List<BoardRankingDTO> getTop5ByCommentsInLast7Days() {
        return boardQnaRepository.findTop5ByCommentsInLast7Days();
    }

    public List<BoardRankingDTO> getTop5ByRecommend(){
        return boardQnaRepository.findTop5ByRecommend();
    }

    public List<BoardRankingDTO> getTop5ByViewsInLast7Days(){
        return boardQnaRepository.findTop5ByViewsInLast7Days();
    }

    public List<BoardQnaEntity> findByTitle(String title) { return boardQnaRepository.findByTitle(title); }

    public List<BoardQnaEntity> findByAuthor(String author) { return boardQnaRepository.findByAuthor(author); }
}
