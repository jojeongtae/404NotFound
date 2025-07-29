package com.example.notfound_backend.data.dao.normalboard.board;

import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
import com.example.notfound_backend.data.entity.enumlist.Status;
import com.example.notfound_backend.data.entity.normalboard.board.BoardFreeEntity;
import com.example.notfound_backend.data.repository.normalboard.board.BoardFreeRepository;
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
    public List<BoardFreeEntity> findAllByStatus(Status status) {
        return boardFreeRepository.findAllByStatus(status);
    }
    public List<BoardFreeEntity> findAllBoardsByUser(String username) {
        return boardFreeRepository.findAllByUser(username);
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

    public List<BoardFreeEntity> findByTitle(String title) { return boardFreeRepository.findByTitle(title); }

    public List<BoardFreeEntity> findByAuthor(String author) { return boardFreeRepository.findByAuthor(author); }
}

