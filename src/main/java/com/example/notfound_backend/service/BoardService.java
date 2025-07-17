package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardDAO;
import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.data.entity.BoardEntity;
import com.example.notfound_backend.data.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardDAO boardDAO;

    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList=boardDAO.findAllBoards();
        List<BoardDTO> boardDTOList=new ArrayList<>();
        for(BoardEntity boardEntity:boardEntityList){
            BoardDTO boardDTO=new BoardDTO();
            boardDTO.setId(boardEntity.getId());
            boardDTO.setTitle(boardEntity.getTitle());
            boardDTO.setBody(boardEntity.getBody());
            boardDTO.setImgsrc(boardEntity.getImgsrc());
            boardDTO.setAuthor(boardEntity.getAuthor());
            boardDTO.setRecommend(boardEntity.getRecommend());
            boardDTO.setViews(boardEntity.getViews());
            boardDTO.setCategory(boardEntity.getCategory());
            boardDTO.setUpdatedAt(boardEntity.getUpdatedAt());
            boardDTOList.add(boardDTO);
        }
        return boardDTOList;
    }

    @Transactional
    public BoardDTO viewBoard(Integer id) {
        boardDAO.incrementViews(id);
        BoardEntity entity=boardDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Board not found"));
        return toDTO(entity);
    }

    private BoardDTO toDTO(BoardEntity entity) {
        return new BoardDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getBody(),
                entity.getImgsrc(),
                entity.getAuthor(),
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
        BoardEntity entity = new BoardEntity();
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(boardDTO.getImgsrc());
        entity.setAuthor(boardDTO.getAuthor());

        entity.setRecommend(0);
        entity.setViews(0); // 새 글이니 조회수 0으로 시작
        entity.setCategory("NORMAL");
        entity.setStatus(Status.VISIBLE);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        BoardEntity saved = boardDAO.save(entity);
        return toDTO(saved);
    }

    @Transactional
    public BoardDTO updateBoard(Integer id, BoardDTO boardDTO) {
        BoardEntity entity = boardDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        // 원하는 필드만 수정
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(boardDTO.getImgsrc());
        entity.setRecommend(boardDTO.getRecommend());
        entity.setCategory(boardDTO.getCategory());
        entity.setStatus(boardDTO.getStatus() != null ? Status.valueOf(boardDTO.getStatus()) : entity.getStatus());
        entity.setUpdatedAt(Instant.now());

        BoardEntity updated = boardDAO.save(entity);
        return toDTO(updated);
    }

    @Transactional
    public void deleteBoard(Integer id) {
        BoardEntity entity = boardDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        boardDAO.delete(entity);
    }
}
