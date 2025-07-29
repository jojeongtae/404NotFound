package com.example.notfound_backend.data.dao.normalboard.board;


import com.example.notfound_backend.data.entity.enumlist.Status;
import com.example.notfound_backend.data.entity.normalboard.board.BoardNoticeEntity;
import com.example.notfound_backend.data.repository.normalboard.board.BoardNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardNoticeDAO {
    private final BoardNoticeRepository boardNoticeRepository;

    public List<BoardNoticeEntity> findAllBoards() {
        return boardNoticeRepository.findAll();
    }

    public List<BoardNoticeEntity> findAllByStatus(Status status) {
        return boardNoticeRepository.findAllByStatus(status);
    }
    public List<BoardNoticeEntity> findAllBoardsByUser(String username) {
        return boardNoticeRepository.findAllByUser(username);
    }

    public BoardNoticeEntity save(BoardNoticeEntity boardNoticeEntity) {
        return boardNoticeRepository.save(boardNoticeEntity);
    }

//    @Transactional
//    public void incrementRecommend(Integer id) {
//        boardNoticeRepository.incrementRecommend(id);
//    }
//
//    @Transactional
//    public void decrementRecommend(Integer id) {boardNoticeRepository.decrementRecommend(id);}

    @Transactional
    public void incrementViews(Integer id) {
        boardNoticeRepository.incrementViews(id);
    }

    public Optional<BoardNoticeEntity> findById(Integer id) {
        return boardNoticeRepository.findById(id);
    }

    public void delete(BoardNoticeEntity boardNoticeEntity) {
        boardNoticeRepository.delete(boardNoticeEntity);
    }

    public List<BoardNoticeEntity> findByTitle(String title) { return boardNoticeRepository.findByTitle(title);}

    public List<BoardNoticeEntity> findByAuthor(String author) { return boardNoticeRepository.findByAuthor(author);}

}
