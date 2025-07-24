package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardFreeDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.data.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardFreeService {
    private final BoardFreeDAO boardFreeDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;
    private final UserInfoService userInfoService;

    public List<BoardDTO> findAll() {
        List<BoardFreeEntity> boardFreeEntityList = boardFreeDAO.findAllBoards();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardFreeEntity boardFreeEntity : boardFreeEntityList){

            if (boardFreeEntity.getStatus() == Status.VISIBLE) { // VISIBLE만 노출
                BoardDTO boardFreeDTO = new BoardDTO();
                boardFreeDTO.setId(boardFreeEntity.getId());
                boardFreeDTO.setTitle(boardFreeEntity.getTitle());
                boardFreeDTO.setBody(boardFreeEntity.getBody());
                boardFreeDTO.setImgsrc(boardFreeEntity.getImgsrc());

                if (boardFreeEntity.getAuthor() != null) {
                    boardFreeDTO.setAuthor(boardFreeEntity.getAuthor().getUsername());
                }
                UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(boardFreeEntity.getAuthor().getUsername());
                String userNickname = userInfoEntity.getNickname();
                String userGrade = userInfoService.getUserGrade(userInfoEntity.getUsername().getUsername());
                boardFreeDTO.setAuthorNickname(userNickname); // 추가
                boardFreeDTO.setGrade(userGrade);
                boardFreeDTO.setRecommend(boardFreeEntity.getRecommend());
                boardFreeDTO.setViews(boardFreeEntity.getViews());
                boardFreeDTO.setCategory(boardFreeEntity.getCategory());
                boardFreeDTO.setCreatedAt(boardFreeEntity.getCreatedAt());
                boardFreeDTO.setUpdatedAt(boardFreeEntity.getUpdatedAt());
                boardFreeDTO.setStatus(boardFreeEntity.getStatus().name());
                boardDTOList.add(boardFreeDTO);
            }
            
        }
        return boardDTOList;
    }

    @Transactional
    public BoardDTO viewBoard(Integer id) {
        boardFreeDAO.incrementViews(id);
        BoardFreeEntity entity= boardFreeDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Board not found"));
        return toDTO(entity);
    }

    private BoardDTO toDTO(BoardFreeEntity entity) {
        UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(entity.getAuthor().getUsername());
        String userNickname = userInfoEntity.getNickname();
        return new BoardDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getBody(),
                entity.getImgsrc(),
                entity.getAuthor().getUsername(),
                userNickname, // 추가
                userInfoService.getUserGrade(userInfoEntity.getUsername().getUsername()),
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
        userInfoService.userStatusValidator(boardDTO.getAuthor());

        BoardFreeEntity entity = new BoardFreeEntity();
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(boardDTO.getImgsrc());

        UserAuthEntity author = userAuthDAO.findByUsername(boardDTO.getAuthor());
        entity.setAuthor(author);

        entity.setRecommend(0);
        entity.setViews(0); // 새 글이니 조회수 0으로 시작
        entity.setCategory("free");
        entity.setStatus(Status.VISIBLE);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        BoardFreeEntity saved = boardFreeDAO.save(entity);
        userInfoDAO.updatePoint(boardDTO.getAuthor(), 3); // 3포인트증가
        return toDTO(saved);
    }

    @Transactional
    public BoardDTO updateBoard(Integer id, BoardDTO boardDTO) {
        userInfoService.userStatusValidator(boardDTO.getAuthor());

        BoardFreeEntity entity = boardFreeDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        // 원하는 필드만 수정
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(boardDTO.getImgsrc());
        entity.setRecommend(boardDTO.getRecommend());
        entity.setCategory(boardDTO.getCategory());
        entity.setStatus(boardDTO.getStatus() != null ? Status.valueOf(boardDTO.getStatus()) : entity.getStatus());
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

    public List<BoardDTO> findByTitle(String title) {
        List<BoardFreeEntity> boardFreeEntities = boardFreeDAO.findByTitle(title);
        return boardFreeEntities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<BoardDTO> findByAuthor(String author) {
        List<BoardFreeEntity> boardFreeEntities = boardFreeDAO.findByAuthor(author);
        return boardFreeEntities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

//    public BoardDTO recommendBoard(Integer id) {
//        boardFreeDAO.incrementRecommend(id);
//        BoardFreeEntity entity= boardFreeDAO.findById(id)
//                .orElseThrow(() -> new RuntimeException("Board not found"));
//        return toDTO(entity);
//    }
//
//    public BoardDTO cancelRecommendBoard(Integer id) {
//        boardFreeDAO.decrementRecommend(id);
//        BoardFreeEntity entity= boardFreeDAO.findById(id)
//                .orElseThrow(() -> new RuntimeException("Board not found"));
//        return toDTO(entity);
//    }

}
