package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardFoodCommentDAO;
import com.example.notfound_backend.data.dao.BoardFoodDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.BoardCommentDTO;
import com.example.notfound_backend.data.entity.BoardFoodCommentEntity;
import com.example.notfound_backend.data.entity.BoardFoodEntity;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardFoodCommentService {

    private final BoardFoodCommentDAO boardFoodCommentDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;
    private final BoardFoodDAO boardFoodDAO;

    @Transactional
    public BoardCommentDTO addComment(BoardCommentDTO dto) {
        BoardFoodCommentEntity entity = new BoardFoodCommentEntity();

        BoardFoodEntity board = boardFoodDAO.findById(dto.getBoardId())
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        entity.setBoard(board);

        UserAuthEntity user = userAuthDAO.findByUsername(dto.getAuthor());
        entity.setAuthor(user);
        entity.setContent(dto.getContent());
        entity.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : Instant.now());

        BoardFoodCommentEntity saved = boardFoodCommentDAO.save(entity);
        userInfoDAO.updatePoint(dto.getAuthor(), 1); // 1포인트증가
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<BoardCommentDTO> getCommentsByBoardId(Integer boardId) {
        return boardFoodCommentDAO.findAllByBoardIdOrderByCreatedAtDesc(boardId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Integer id) {
        BoardFoodCommentEntity entity = boardFoodCommentDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));
        boardFoodCommentDAO.delete(entity);
    }

    private BoardCommentDTO toDTO(BoardFoodCommentEntity entity) {
        return BoardCommentDTO.builder()
                .id(entity.getId())
                .boardId(entity.getBoard().getId())
                .author(entity.getAuthor().getUsername())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
