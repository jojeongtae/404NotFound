package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardDAO;
import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.data.entity.BoardEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                entity.getStatus()
        );
    }
}
