package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardFreeDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.data.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardFreeService {
    private final BoardFreeDAO boardFreeDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;
    private final UserInfoService userInfoService;
    private final UploadImageService uploadImageService;

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
    public BoardDTO createBoard(BoardDTO boardDTO, MultipartFile file) throws IOException {
        userInfoService.userStatusValidator(boardDTO.getAuthor());
        String imgsrc = uploadImageService.uploadBoardImage(file); // 이미지파일 저장

        BoardFreeEntity entity = BoardFreeEntity.builder()
                .title(boardDTO.getTitle())
                .body(boardDTO.getBody())
                .imgsrc(imgsrc)
                .author(userAuthDAO.findByUsername(boardDTO.getAuthor()))
                .recommend(0)
                .views(0)
                .category("free")
                .status(Status.VISIBLE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        BoardFreeEntity saved = boardFreeDAO.save(entity);
        userInfoDAO.updatePoint(boardDTO.getAuthor(), 3); // 3포인트증가
        return toDTO(saved);
    }


    @Transactional
    public BoardDTO updateBoard(Integer id, BoardDTO boardDTO, MultipartFile file) throws IOException {
        userInfoService.userStatusValidator(boardDTO.getAuthor());

        BoardFreeEntity entity = boardFreeDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        String imgsrc = uploadImageService.updateBoardImage(file, entity.getImgsrc()); // 이미지파일 수정

        // 원하는 필드만 수정
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(imgsrc);
//        entity.setRecommend(boardDTO.getRecommend());
//        entity.setCategory(boardDTO.getCategory());
        entity.setStatus(boardDTO.getStatus() != null ? Status.valueOf(boardDTO.getStatus()) : entity.getStatus());
        entity.setUpdatedAt(Instant.now());

        BoardFreeEntity updated = boardFreeDAO.save(entity);
        return toDTO(updated);
    }

    @Transactional
    public void deleteBoard(Integer id) throws IOException {
        BoardFreeEntity entity = boardFreeDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        uploadImageService.deleteBoardImage(entity.getImgsrc()); // 이미지파일 삭제
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
