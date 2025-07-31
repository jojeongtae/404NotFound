package com.example.notfound_backend.data.dao.normalboard.board;

import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
import com.example.notfound_backend.data.entity.enumlist.Status;
import com.example.notfound_backend.data.entity.normalboard.board.BoardInfoEntity;
import com.example.notfound_backend.data.repository.normalboard.board.BoardInfoRepository;
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

    public List<BoardInfoEntity> findAllByStatus(Status status) {
        return boardInfoRepository.findAllByStatus(status);
    }

    public List<BoardInfoEntity> findAllBoardsByUser(String username) {
        return boardInfoRepository.findAllByUser(username);
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

    public List<BoardRankingDTO> getTop5ByRecommendInLast7Days() {
        return boardInfoRepository.findTop5ByRecommendInLast7Days();
    }

    public List<BoardRankingDTO> getTop5ByCommentsInLast7Days() {
        return boardInfoRepository.findTop5ByCommentsInLast7Days();
    }

    public List<BoardRankingDTO> getTop5ByRecommend(){
        return boardInfoRepository.findTop5ByRecommend();
    }

    public List<BoardRankingDTO> getTop5ByViewsInLast7Days() {
        return boardInfoRepository.findTop5ByViewsInLast7Days();
    }

    public List<BoardInfoEntity> findByTitle(String title){ return boardInfoRepository.findByTitle(title); }

    public List<BoardInfoEntity> findByAuthor(String author){ return boardInfoRepository.findByAuthor(author); }

}
