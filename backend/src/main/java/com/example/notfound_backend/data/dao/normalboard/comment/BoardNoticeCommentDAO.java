package com.example.notfound_backend.data.dao.normalboard.comment;

import com.example.notfound_backend.data.entity.normalboard.comment.BoardNoticeCommentEntity;
import com.example.notfound_backend.data.repository.normalboard.comment.BoardNoticeCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardNoticeCommentDAO {

    private final BoardNoticeCommentRepository boardNoticeCommentRepository;

    public Optional<BoardNoticeCommentEntity> findById(Integer id) {
        return boardNoticeCommentRepository.findById(id);
    }

    public List<BoardNoticeCommentEntity> findAllByBoardIdOrderByCreatedAtDesc(Integer boardId) {
        return boardNoticeCommentRepository.findAllByBoardIdOrderByCreatedAtDesc(boardId);
    }

    public BoardNoticeCommentEntity save(BoardNoticeCommentEntity entity){
        return boardNoticeCommentRepository.save(entity);
    }

    public void delete(BoardNoticeCommentEntity entity){boardNoticeCommentRepository.delete(entity);}

}
