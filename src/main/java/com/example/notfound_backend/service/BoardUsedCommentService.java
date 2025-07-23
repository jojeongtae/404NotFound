package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardUsedCommentDAO;
import com.example.notfound_backend.data.dao.BoardUsedDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.BoardCommentDTO;
import com.example.notfound_backend.data.entity.*;
import com.example.notfound_backend.exception.UserSuspendedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardUsedCommentService {

    private final BoardUsedCommentDAO boardUsedCommentDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;
    private final UserInfoService userInfoService;
    private final BoardUsedDAO boardUsedDAO;

    @Transactional
    public BoardCommentDTO addComment(BoardCommentDTO dto) {
        UserStatus userStatus = userInfoDAO.getUserInfo(dto.getAuthor()).getStatus();
        if (userStatus != UserStatus.ACTIVE) {
            throw new UserSuspendedException("활동 정지된 사용자입니다.");
        }
        BoardUsedCommentEntity entity = new BoardUsedCommentEntity();

        BoardUsedEntity board = boardUsedDAO.findById(dto.getBoardId())
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        entity.setBoard(board);

        UserAuthEntity user = userAuthDAO.findByUsername(dto.getAuthor());
        entity.setAuthor(user);
        entity.setContent(dto.getContent());
        entity.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : Instant.now());
        entity.setParentId(dto.getParentId());

        BoardUsedCommentEntity saved = boardUsedCommentDAO.save(entity);



        userInfoDAO.updatePoint(dto.getAuthor(), 1); // 1포인트증가
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<BoardCommentDTO> getCommentsByBoardId(Integer boardId) {
        return boardUsedCommentDAO.findAllByBoardIdOrderByCreatedAtDesc(boardId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Integer id) {
        BoardUsedCommentEntity entity = boardUsedCommentDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));
        boardUsedCommentDAO.delete(entity);
    }

    private BoardCommentDTO toDTO(BoardUsedCommentEntity entity) {
        UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(entity.getAuthor().getUsername());
        return BoardCommentDTO.builder()
                .id(entity.getId())
                .boardId(entity.getBoard().getId())
                .parentId(entity.getParentId())
                .author(entity.getAuthor().getUsername())
                .authorNickname(userInfoEntity.getNickname())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .grade(userInfoService.getUserGrade(userInfoEntity.getUsername().getUsername()))
                .build();
    }

}
