package com.example.notfound_backend.data.dao;


import com.example.notfound_backend.data.entity.BoardNoticeEntity;
import com.example.notfound_backend.data.repository.BoardNoticeRepository;
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

    public BoardNoticeEntity save(BoardNoticeEntity boardNoticeEntity) {
        return boardNoticeRepository.save(boardNoticeEntity);
    }

    @Transactional
    public void incrementRecommend(Integer id) {
        boardNoticeRepository.incrementRecommend(id);
    }

    @Transactional
    public void decrementRecommend(Integer id) {boardNoticeRepository.decrementRecommend(id);}

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

}
