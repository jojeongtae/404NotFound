package com.example.notfound_backend.service;


import com.example.notfound_backend.data.dao.BoardFoodDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.data.entity.BoardFoodEntity;
import com.example.notfound_backend.data.entity.BoardFreeEntity;
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
public class BoardFoodService {

    private final BoardFoodDAO boardFoodDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;

    public List<BoardDTO> findAll() {
        List<BoardFoodEntity> boardFoodEntityList = boardFoodDAO.findAllBoards();
        List<BoardDTO> boardDTOList =new ArrayList<>();
        for(BoardFoodEntity boardFoodEntity : boardFoodEntityList){
            BoardDTO boardFoodDTO =new BoardDTO();
            boardFoodDTO.setId(boardFoodEntity.getId());
            boardFoodDTO.setTitle(boardFoodEntity.getTitle());
            boardFoodDTO.setBody(boardFoodEntity.getBody());
            boardFoodDTO.setImgsrc(boardFoodEntity.getImgsrc());

            if (boardFoodEntity.getAuthor() != null) {
                boardFoodDTO.setAuthor(boardFoodEntity.getAuthor().getUsername());
            }

            boardFoodDTO.setRecommend(boardFoodEntity.getRecommend());
            boardFoodDTO.setViews(boardFoodEntity.getViews());
            boardFoodDTO.setCategory(boardFoodEntity.getCategory());
            boardFoodDTO.setCreatedAt(boardFoodEntity.getCreatedAt());
            boardFoodDTO.setUpdatedAt(boardFoodEntity.getUpdatedAt());
            boardFoodDTO.setStatus(boardFoodEntity.getStatus().name());
            boardDTOList.add(boardFoodDTO);
        }
        return boardDTOList;
    }

    @Transactional
    public BoardDTO viewBoard(Integer id) {
        boardFoodDAO.incrementViews(id);
        BoardFoodEntity entity= boardFoodDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Board not found"));
        return toDTO(entity);
    }

    private BoardDTO toDTO(BoardFoodEntity entity) {
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
        BoardFoodEntity entity = new BoardFoodEntity();
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(boardDTO.getImgsrc());

        UserAuthEntity author = userAuthDAO.findByUsername(boardDTO.getAuthor());
        entity.setAuthor(author);

        entity.setRecommend(0);
        entity.setViews(0); // 새 글이니 조회수 0으로 시작
        entity.setCategory("FOOD");
        entity.setStatus(Status.VISIBLE);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        BoardFoodEntity saved = boardFoodDAO.save(entity);
        userInfoDAO.updatePoint(boardDTO.getAuthor(), 3); // 3포인트증가
        return toDTO(saved);
    }

    @Transactional
    public BoardDTO updateBoard(Integer id, BoardDTO boardDTO) {
        BoardFoodEntity entity = boardFoodDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        // 원하는 필드만 수정
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(boardDTO.getImgsrc());
        entity.setRecommend(boardDTO.getRecommend());
        entity.setCategory(boardDTO.getCategory());
        entity.setStatus(boardDTO.getStatus() != null ? Status.valueOf(boardDTO.getStatus()) : entity.getStatus());
        entity.setUpdatedAt(Instant.now());

        BoardFoodEntity updated = boardFoodDAO.save(entity);
        return toDTO(updated);
    }

    @Transactional
    public void deleteBoard(Integer id) {
        BoardFoodEntity entity = boardFoodDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        boardFoodDAO.delete(entity);
    }

    public BoardDTO recommendBoard(Integer id) {
        boardFoodDAO.incrementRecommend(id);
        BoardFoodEntity entity= boardFoodDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        return toDTO(entity);
    }

    public BoardDTO cancelRecommendBoard(Integer id) {
        boardFoodDAO.decrementRecommend(id);
        BoardFoodEntity entity= boardFoodDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        return toDTO(entity);
    }

}
