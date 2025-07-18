package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardUsedDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.data.entity.BoardUsedEntity;
import com.example.notfound_backend.data.entity.Status;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardUsedService {

    private final BoardUsedDAO boardUsedDAO;
    private final UserAuthDAO userAuthDAO;

    public List<BoardDTO> findAll() {
        List<BoardUsedEntity> boardUsedEntityList = boardUsedDAO.findAllBoards();
        List<BoardDTO> boardDTOList =new ArrayList<>();
        for(BoardUsedEntity boardUsedEntity : boardUsedEntityList){
            BoardDTO boardUsedDTO =new BoardDTO();
            boardUsedDTO.setId(boardUsedEntity.getId());
            boardUsedDTO.setTitle(boardUsedEntity.getTitle());
            boardUsedDTO.setBody(boardUsedEntity.getBody());
            boardUsedDTO.setImgsrc(boardUsedEntity.getImgsrc());

            if (boardUsedEntity.getAuthor() != null) {
                boardUsedDTO.setAuthor(boardUsedEntity.getAuthor().getUsername());
            }

            boardUsedDTO.setRecommend(boardUsedEntity.getRecommend());
            boardUsedDTO.setViews(boardUsedEntity.getViews());
            boardUsedDTO.setCategory(boardUsedEntity.getCategory());
            boardUsedDTO.setCreatedAt(boardUsedEntity.getCreatedAt());
            boardUsedDTO.setUpdatedAt(boardUsedEntity.getUpdatedAt());
            boardUsedDTO.setStatus(boardUsedEntity.getStatus().name());
            boardDTOList.add(boardUsedDTO);
        }
        return boardDTOList;
    }

    @Transactional
    public BoardDTO viewBoard(Integer id) {
        boardUsedDAO.incrementViews(id);
        BoardUsedEntity entity= boardUsedDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Board not found"));
        return toDTO(entity);
    }

    private BoardDTO toDTO(BoardUsedEntity entity) {
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
        BoardUsedEntity entity = new BoardUsedEntity();
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(boardDTO.getImgsrc());

        UserAuthEntity author = userAuthDAO.findByUsername(boardDTO.getAuthor());
        entity.setAuthor(author);

        entity.setRecommend(0);
        entity.setViews(0); // 새 글이니 조회수 0으로 시작
        entity.setCategory("USED");
        entity.setStatus(Status.VISIBLE);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        BoardUsedEntity saved = boardUsedDAO.save(entity);
        return toDTO(saved);
    }

    @Transactional
    public BoardDTO updateBoard(Integer id, BoardDTO boardDTO) {
        BoardUsedEntity entity = boardUsedDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        // 원하는 필드만 수정
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(boardDTO.getImgsrc());
        entity.setRecommend(boardDTO.getRecommend());
        entity.setCategory(boardDTO.getCategory());
        entity.setStatus(boardDTO.getStatus() != null ? Status.valueOf(boardDTO.getStatus()) : entity.getStatus());
        entity.setUpdatedAt(Instant.now());

        BoardUsedEntity updated = boardUsedDAO.save(entity);
        return toDTO(updated);
    }

    @Transactional
    public void deleteBoard(Integer id) {
        BoardUsedEntity entity = boardUsedDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        boardUsedDAO.delete(entity);
    }

    public BoardDTO recommendBoard(Integer id) {
        boardUsedDAO.incrementRecommend(id);
        BoardUsedEntity entity= boardUsedDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        return toDTO(entity);
    }

}
