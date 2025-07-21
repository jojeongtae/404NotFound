package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardFoodCommentDAO;
import com.example.notfound_backend.data.dao.BoardFoodDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dto.BoardFoodCommentDTO;
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
    private final BoardFoodDAO boardFoodDAO;

    @Transactional
    public BoardFoodCommentDTO addComment(BoardFoodCommentDTO dto) {
        BoardFoodCommentEntity entity = new BoardFoodCommentEntity();

        BoardFoodEntity board = boardFoodDAO.findById(dto.getBoardId())
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        entity.setBoard(board);

        UserAuthEntity user = userAuthDAO.findByUsername(dto.getAuthor());
        entity.setAuthor(user);
        entity.setContent(dto.getContent());
        entity.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : Instant.now());

        BoardFoodCommentEntity saved = boardFoodCommentDAO.save(entity);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<BoardFoodCommentDTO> getCommentsByBoardId(Integer boardId) {
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

    private BoardFoodCommentDTO toDTO(BoardFoodCommentEntity entity) {
        return BoardFoodCommentDTO.builder()
                .id(entity.getId())
                .boardId(entity.getBoard().getId())
                .author(entity.getAuthor().getUsername())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
