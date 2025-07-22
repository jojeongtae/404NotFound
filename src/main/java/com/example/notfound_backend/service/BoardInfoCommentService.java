package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardInfoCommentDAO;
import com.example.notfound_backend.data.dao.BoardInfoDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.BoardCommentDTO;
import com.example.notfound_backend.data.entity.BoardInfoCommentEntity;
import com.example.notfound_backend.data.entity.BoardInfoEntity;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.data.entity.UserStatus;
import com.example.notfound_backend.exception.UserSuspendedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardInfoCommentService {

    private final BoardInfoCommentDAO boardInfoCommentDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;
    private final BoardInfoDAO boardInfoDAO;

    @Transactional
    public BoardCommentDTO addComment(BoardCommentDTO dto) {
        UserStatus userStatus = userInfoDAO.getUserInfo(dto.getAuthor()).getStatus();
        if (userStatus != UserStatus.ACTIVE) {
            throw new UserSuspendedException("활동 정지된 사용자입니다.");
        }
        BoardInfoCommentEntity entity = new BoardInfoCommentEntity();

        BoardInfoEntity board = boardInfoDAO.findById(dto.getBoardId())
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        entity.setBoard(board);

        UserAuthEntity user = userAuthDAO.findByUsername(dto.getAuthor());
        entity.setAuthor(user);
        entity.setContent(dto.getContent());
        entity.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : Instant.now());
        entity.setParentId(dto.getParentId());

        BoardInfoCommentEntity saved = boardInfoCommentDAO.save(entity);
        userInfoDAO.updatePoint(dto.getAuthor(), 1); // 1포인트증가
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<BoardCommentDTO> getCommentsByBoardId(Integer boardId) {
        return boardInfoCommentDAO.findAllByBoardIdOrderByCreatedAtDesc(boardId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Integer id) {
        BoardInfoCommentEntity entity = boardInfoCommentDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));
        boardInfoCommentDAO.delete(entity);
    }

    private BoardCommentDTO toDTO(BoardInfoCommentEntity entity) {
        return BoardCommentDTO.builder()
                .id(entity.getId())
                .boardId(entity.getBoard().getId())
                .parentId(entity.getParentId())
                .author(entity.getAuthor().getUsername())
                .authorNickname(userInfoDAO.getUserInfo(entity.getAuthor().getUsername()).getNickname())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
