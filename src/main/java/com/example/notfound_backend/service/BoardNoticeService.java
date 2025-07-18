package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardNoticeDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.data.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardNoticeService {
    private final BoardNoticeDAO boardNoticeDAO;
    private final UserAuthDAO userAuthDAO;

    public List<BoardDTO> findAll() {
        List<BoardNoticeEntity> boardNoticeEntityList = boardNoticeDAO.findAllBoards();
        List<BoardDTO> boardDTOList =new ArrayList<>();
        for(BoardNoticeEntity boardNoticeEntity : boardNoticeEntityList){
            BoardDTO boardNoticeDTO =new BoardDTO();
            boardNoticeDTO.setId(boardNoticeEntity.getId());
            boardNoticeDTO.setTitle(boardNoticeEntity.getTitle());
            boardNoticeDTO.setBody(boardNoticeEntity.getBody());
            boardNoticeDTO.setImgsrc(boardNoticeEntity.getImgsrc());

            if (boardNoticeEntity.getAuthor() != null) {
                boardNoticeDTO.setAuthor(boardNoticeEntity.getAuthor().getUsername());
            }

            boardNoticeDTO.setRecommend(boardNoticeEntity.getRecommend());
            boardNoticeDTO.setViews(boardNoticeEntity.getViews());
            boardNoticeDTO.setCategory(boardNoticeEntity.getCategory());
            boardNoticeDTO.setCreatedAt(boardNoticeEntity.getCreatedAt());
            boardNoticeDTO.setUpdatedAt(boardNoticeEntity.getUpdatedAt());
            boardNoticeDTO.setStatus(boardNoticeEntity.getStatus().name());
            boardDTOList.add(boardNoticeDTO);
        }
        return boardDTOList;
    }

    @Transactional
    public BoardDTO viewBoard(Integer id) {
        boardNoticeDAO.incrementViews(id);
        BoardNoticeEntity entity= boardNoticeDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Board not found"));
        return toDTO(entity);
    }

    private BoardDTO toDTO(BoardNoticeEntity entity) {
        return new BoardDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getBody(),
                entity.getImgsrc(),
                entity.getAuthor().getUsername(),
                entity.getRecommend(),
                entity.getViews(),
                entity.getCategory(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getStatus() != null ? entity.getStatus().name() : null
        );
    }

    @Transactional
    public BoardDTO createBoard(BoardDTO boardDTO) {
        UserAuthEntity author = userAuthDAO.findByUsername(boardDTO.getAuthor());
        if (!"ROLE_ADMIN".equals(author.getRole())) {
            throw new AccessDeniedException("관리자만 글을 작성할 수 있습니다.");
        }

        BoardNoticeEntity entity = new BoardNoticeEntity();
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(boardDTO.getImgsrc());
        entity.setAuthor(author);
        entity.setRecommend(0);
        entity.setViews(0);
        entity.setCategory("NORMAL");
        entity.setStatus(Status.VISIBLE);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        BoardNoticeEntity saved = boardNoticeDAO.save(entity);
        return toDTO(saved);
    }

    @Transactional
    public BoardDTO updateBoard(Integer id, BoardDTO boardDTO) {

        BoardNoticeEntity entity = boardNoticeDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        UserAuthEntity author = userAuthDAO.findByUsername(boardDTO.getAuthor());
        if (!"ROLE_ADMIN".equals(author.getRole())) {
            throw new AccessDeniedException("관리자만 글을 작성할 수 있습니다.");
        }

        // 원하는 필드만 수정
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(boardDTO.getImgsrc());
        entity.setRecommend(boardDTO.getRecommend());
        entity.setCategory(boardDTO.getCategory());
        entity.setStatus(boardDTO.getStatus() != null ? Status.valueOf(boardDTO.getStatus()) : entity.getStatus());
        entity.setUpdatedAt(Instant.now());

        BoardNoticeEntity updated = boardNoticeDAO.save(entity);
        return toDTO(updated);
    }

//    @Transactional
//    public void deleteBoard(Integer id, String username) {
//        BoardNoticeEntity entity = boardNoticeDAO.findById(id)
//                .orElseThrow(() -> new RuntimeException("Board not found"));
//
//        UserAuthEntity user = userAuthDAO.findByUsername(username);
//        if (!"ROLE_ADMIN".equals(user.getRole())) {
//            throw new AccessDeniedException("관리자만 삭제할 수 있습니다.");
//        }
//
//        boardNoticeDAO.delete(entity);
//    }

    @Transactional
    public void deleteBoard(Integer id, String username) {

        BoardNoticeEntity entity = boardNoticeDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        UserAuthEntity user = userAuthDAO.findByUsername(username);
        if (user == null || !user.getRole().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("관리자만 삭제할 수 있습니다.");
        }
        boardNoticeDAO.delete(entity);
    }

    public BoardDTO recommendBoard(Integer id) {
        boardNoticeDAO.incrementRecommend(id);
        BoardNoticeEntity entity= boardNoticeDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        return toDTO(entity);
    }

    public BoardDTO cancelRecommendBoard(Integer id) {
        boardNoticeDAO.decrementRecommend(id);
        BoardNoticeEntity entity= boardNoticeDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        return toDTO(entity);
    }

}
