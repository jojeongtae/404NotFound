package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardFreeDAO;
import com.example.notfound_backend.data.entity.BoardFreeEntity;
import com.example.notfound_backend.data.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardFreeService {
    private final BoardFreeDAO boardFreeDAO;

    public List<com.example.notfound_backend.data.dto.BoardFreeDTO> findAll() {
        List<BoardFreeEntity> boardFreeEntityList = boardFreeDAO.findAllBoards();
        List<com.example.notfound_backend.data.dto.BoardFreeDTO> boardFreeDTOList =new ArrayList<>();
        for(BoardFreeEntity boardFreeEntity : boardFreeEntityList){
            com.example.notfound_backend.data.dto.BoardFreeDTO boardFreeDTO =new com.example.notfound_backend.data.dto.BoardFreeDTO();
            boardFreeDTO.setId(boardFreeEntity.getId());
            boardFreeDTO.setTitle(boardFreeEntity.getTitle());
            boardFreeDTO.setBody(boardFreeEntity.getBody());
            boardFreeDTO.setImgsrc(boardFreeEntity.getImgsrc());
            boardFreeDTO.setAuthor(boardFreeEntity.getAuthor());
            boardFreeDTO.setRecommend(boardFreeEntity.getRecommend());
            boardFreeDTO.setViews(boardFreeEntity.getViews());
            boardFreeDTO.setCategory(boardFreeEntity.getCategory());
            boardFreeDTO.setCreatedAt(boardFreeEntity.getCreatedAt());
            boardFreeDTO.setUpdatedAt(boardFreeEntity.getUpdatedAt());
            boardFreeDTO.setStatus(boardFreeEntity.getStatus().name());
            boardFreeDTOList.add(boardFreeDTO);
        }
        return boardFreeDTOList;
    }

    @Transactional
    public com.example.notfound_backend.data.dto.BoardFreeDTO viewBoard(Integer id) {
        boardFreeDAO.incrementViews(id);
        BoardFreeEntity entity= boardFreeDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Board not found"));
        return toDTO(entity);
    }

    private com.example.notfound_backend.data.dto.BoardFreeDTO toDTO(BoardFreeEntity entity) {
        return new com.example.notfound_backend.data.dto.BoardFreeDTO(
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
    public com.example.notfound_backend.data.dto.BoardFreeDTO createBoard(com.example.notfound_backend.data.dto.BoardFreeDTO boardFreeDTO) {
        BoardFreeEntity entity = new BoardFreeEntity();
        entity.setTitle(boardFreeDTO.getTitle());
        entity.setBody(boardFreeDTO.getBody());
        entity.setImgsrc(boardFreeDTO.getImgsrc());
        entity.setAuthor(boardFreeDTO.getAuthor());

        entity.setRecommend(0);
        entity.setViews(0); // 새 글이니 조회수 0으로 시작
        entity.setCategory("NORMAL");
        entity.setStatus(Status.VISIBLE);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        BoardFreeEntity saved = boardFreeDAO.save(entity);
        return toDTO(saved);
    }

    @Transactional
    public com.example.notfound_backend.data.dto.BoardFreeDTO updateBoard(Integer id, com.example.notfound_backend.data.dto.BoardFreeDTO boardFreeDTO) {
        BoardFreeEntity entity = boardFreeDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        // 원하는 필드만 수정
        entity.setTitle(boardFreeDTO.getTitle());
        entity.setBody(boardFreeDTO.getBody());
        entity.setImgsrc(boardFreeDTO.getImgsrc());
        entity.setRecommend(boardFreeDTO.getRecommend());
        entity.setCategory(boardFreeDTO.getCategory());
        entity.setStatus(boardFreeDTO.getStatus() != null ? Status.valueOf(boardFreeDTO.getStatus()) : entity.getStatus());
        entity.setUpdatedAt(Instant.now());

        BoardFreeEntity updated = boardFreeDAO.save(entity);
        return toDTO(updated);
    }

    @Transactional
    public void deleteBoard(Integer id) {
        BoardFreeEntity entity = boardFreeDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        boardFreeDAO.delete(entity);
    }

    public com.example.notfound_backend.data.dto.BoardFreeDTO recommendBoard(Integer id) {
        boardFreeDAO.incrementRecommend(id);
        BoardFreeEntity entity= boardFreeDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        return toDTO(entity);
    }
}
