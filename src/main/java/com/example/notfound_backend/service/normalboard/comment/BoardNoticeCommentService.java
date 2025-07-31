package com.example.notfound_backend.service.normalboard.comment;

import com.example.notfound_backend.data.dao.normalboard.comment.BoardNoticeCommentDAO;
import com.example.notfound_backend.data.dao.normalboard.board.BoardNoticeDAO;
import com.example.notfound_backend.data.dao.login.UserAuthDAO;
import com.example.notfound_backend.data.dao.admin.UserInfoDAO;
import com.example.notfound_backend.data.dto.normalboard.BoardCommentDTO;
import com.example.notfound_backend.data.entity.admin.UserInfoEntity;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.normalboard.board.BoardNoticeEntity;
import com.example.notfound_backend.data.entity.normalboard.comment.BoardNoticeCommentEntity;
import com.example.notfound_backend.service.admin.UserInfoService;
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
    private final UserInfoService userInfoService;
    private final BoardNoticeDAO boardNoticeDAO;

    @Transactional
    public BoardCommentDTO addComment(BoardCommentDTO dto) {
        userInfoService.userStatusValidator(dto.getAuthor());

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
