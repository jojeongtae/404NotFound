package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardNoticeCommentDAO;
import com.example.notfound_backend.data.dao.BoardNoticeDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.BoardCommentDTO;
import com.example.notfound_backend.data.entity.BoardNoticeCommentEntity;
import com.example.notfound_backend.data.entity.BoardNoticeEntity;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardNoticeCommentService {

    private final BoardNoticeCommentDAO boardNoticeCommentDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;
    private final BoardNoticeDAO boardNoticeDAO;

    @Transactional
    public BoardCommentDTO addComment(BoardCommentDTO dto) {
        BoardNoticeCommentEntity entity = new BoardNoticeCommentEntity();

        BoardNoticeEntity board = boardNoticeDAO.findById(dto.getBoardId())
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        entity.setBoard(board);

        UserAuthEntity user = userAuthDAO.findByUsername(dto.getAuthor());
        entity.setAuthor(user);
        entity.setContent(dto.getContent());
        entity.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : Instant.now());
        entity.setParentId(dto.getParentId());

        BoardNoticeCommentEntity saved = boardNoticeCommentDAO.save(entity);
        userInfoDAO.updatePoint(dto.getAuthor(), 1); // 1포인트증가
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<BoardCommentDTO> getCommentsByBoardId(Integer boardId) {
        return boardNoticeCommentDAO.findAllByBoardIdOrderByCreatedAtDesc(boardId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Integer id) {
        BoardNoticeCommentEntity entity = boardNoticeCommentDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));
        boardNoticeCommentDAO.delete(entity);
    }

    private BoardCommentDTO toDTO(BoardNoticeCommentEntity entity) {
        return BoardCommentDTO.builder()
                .id(entity.getId())
                .boardId(entity.getBoard().getId())
                .parentId(entity.getParentId())
                .author(entity.getAuthor().getUsername())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
