package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardInfoDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.data.entity.BoardFoodEntity;
import com.example.notfound_backend.data.entity.BoardInfoEntity;
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
public class BoardInfoService {

    private final BoardInfoDAO boardInfoDAO;
    private final UserAuthDAO userAuthDAO;

    public List<BoardDTO> findAll() {
        List<BoardInfoEntity> boardInfoEntityList = boardInfoDAO.findAllBoards();
        List<BoardDTO> boardDTOList =new ArrayList<>();
        for(BoardInfoEntity boardInfoEntity : boardInfoEntityList){
            BoardDTO boardInfoDTO =new BoardDTO();
            boardInfoDTO.setId(boardInfoEntity.getId());
            boardInfoDTO.setTitle(boardInfoEntity.getTitle());
            boardInfoDTO.setBody(boardInfoEntity.getBody());
            boardInfoDTO.setImgsrc(boardInfoEntity.getImgsrc());

            if (boardInfoEntity.getAuthor() != null) {
                boardInfoDTO.setAuthor(boardInfoEntity.getAuthor().getUsername());
            }

            boardInfoDTO.setRecommend(boardInfoEntity.getRecommend());
            boardInfoDTO.setViews(boardInfoEntity.getViews());
            boardInfoDTO.setCategory(boardInfoEntity.getCategory());
            boardInfoDTO.setCreatedAt(boardInfoEntity.getCreatedAt());
            boardInfoDTO.setUpdatedAt(boardInfoEntity.getUpdatedAt());
            boardInfoDTO.setStatus(boardInfoEntity.getStatus().name());
            boardDTOList.add(boardInfoDTO);
        }
        return boardDTOList;
    }

    @Transactional
    public BoardDTO viewBoard(Integer id) {
        boardInfoDAO.incrementViews(id);
        BoardInfoEntity entity= boardInfoDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Board not found"));
        return toDTO(entity);
    }

    private BoardDTO toDTO(BoardInfoEntity entity) {
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
        BoardInfoEntity entity = new BoardInfoEntity();
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
        BoardInfoEntity saved = boardInfoDAO.save(entity);
        return toDTO(saved);
    }

    @Transactional
    public BoardDTO updateBoard(Integer id, BoardDTO boardDTO) {
        BoardInfoEntity entity = boardInfoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        // 원하는 필드만 수정
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(boardDTO.getImgsrc());
        entity.setRecommend(boardDTO.getRecommend());
        entity.setCategory(boardDTO.getCategory());
        entity.setStatus(boardDTO.getStatus() != null ? Status.valueOf(boardDTO.getStatus()) : entity.getStatus());
        entity.setUpdatedAt(Instant.now());

        BoardInfoEntity updated = boardInfoDAO.save(entity);
        return toDTO(updated);
    }

    @Transactional
    public void deleteBoard(Integer id) {
        BoardInfoEntity entity = boardInfoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        boardInfoDAO.delete(entity);
    }

    public BoardDTO recommendBoard(Integer id) {
        boardInfoDAO.incrementRecommend(id);
        BoardInfoEntity entity= boardInfoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        return toDTO(entity);
    }

    public BoardDTO cancelRecommendBoard(Integer id) {
        boardInfoDAO.decrementRecommend(id);
        BoardInfoEntity entity= boardInfoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        return toDTO(entity);
    }

}
