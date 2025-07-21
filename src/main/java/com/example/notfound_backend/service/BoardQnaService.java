package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardQnaDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.data.entity.BoardFoodEntity;
import com.example.notfound_backend.data.entity.BoardQnaEntity;
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
public class BoardQnaService {

    private final BoardQnaDAO boardQnaDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;

    public List<BoardDTO> findAll() {
        List<BoardQnaEntity> boardQnaEntityList = boardQnaDAO.findAllBoards();
        List<BoardDTO> boardDTOList =new ArrayList<>();
        for(BoardQnaEntity boardQnaEntity : boardQnaEntityList){
            BoardDTO boardQnaDTO =new BoardDTO();
            boardQnaDTO.setId(boardQnaEntity.getId());
            boardQnaDTO.setTitle(boardQnaEntity.getTitle());
            boardQnaDTO.setBody(boardQnaEntity.getBody());
            boardQnaDTO.setImgsrc(boardQnaEntity.getImgsrc());

            if (boardQnaEntity.getAuthor() != null) {
                boardQnaDTO.setAuthor(boardQnaEntity.getAuthor().getUsername());
            }

            boardQnaDTO.setRecommend(boardQnaEntity.getRecommend());
            boardQnaDTO.setViews(boardQnaEntity.getViews());
            boardQnaDTO.setCategory(boardQnaEntity.getCategory());
            boardQnaDTO.setCreatedAt(boardQnaEntity.getCreatedAt());
            boardQnaDTO.setUpdatedAt(boardQnaEntity.getUpdatedAt());
            boardQnaDTO.setStatus(boardQnaEntity.getStatus().name());
            boardDTOList.add(boardQnaDTO);
        }
        return boardDTOList;
    }

    @Transactional
    public BoardDTO viewBoard(Integer id) {
        boardQnaDAO.incrementViews(id);
        BoardQnaEntity entity= boardQnaDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Board not found"));
        return toDTO(entity);
    }

    private BoardDTO toDTO(BoardQnaEntity entity) {
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
        BoardQnaEntity entity = new BoardQnaEntity();
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
        BoardQnaEntity saved = boardQnaDAO.save(entity);
        userInfoDAO.updatePoint(boardDTO.getAuthor(), 3); // 3포인트증가
        return toDTO(saved);
    }

    @Transactional
    public BoardDTO updateBoard(Integer id, BoardDTO boardDTO) {
        BoardQnaEntity entity = boardQnaDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        // 원하는 필드만 수정
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(boardDTO.getImgsrc());
        entity.setRecommend(boardDTO.getRecommend());
        entity.setCategory(boardDTO.getCategory());
        entity.setStatus(boardDTO.getStatus() != null ? Status.valueOf(boardDTO.getStatus()) : entity.getStatus());
        entity.setUpdatedAt(Instant.now());

        BoardQnaEntity updated = boardQnaDAO.save(entity);
        return toDTO(updated);
    }

    @Transactional
    public void deleteBoard(Integer id) {
        BoardQnaEntity entity = boardQnaDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        boardQnaDAO.delete(entity);
    }

    public BoardDTO recommendBoard(Integer id) {
        boardQnaDAO.incrementRecommend(id);
        BoardQnaEntity entity= boardQnaDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        return toDTO(entity);
    }

    public BoardDTO cancelRecommendBoard(Integer id) {
        boardQnaDAO.decrementRecommend(id);
        BoardQnaEntity entity= boardQnaDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        return toDTO(entity);
    }

}
