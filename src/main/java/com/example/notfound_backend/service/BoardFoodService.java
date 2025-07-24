package com.example.notfound_backend.service;


import com.example.notfound_backend.data.dao.BoardFoodDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.BoardDTO;
import com.example.notfound_backend.data.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardFoodService {

    private final BoardFoodDAO boardFoodDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;
    private final UserInfoService userInfoService;
    private final UploadImageService uploadImageService;

    public List<BoardDTO> findAll() {
        List<BoardFoodEntity> boardFoodEntityList = boardFoodDAO.findAllBoards();
        List<BoardDTO> boardDTOList =new ArrayList<>();
        for(BoardFoodEntity boardFoodEntity : boardFoodEntityList){
            if (boardFoodEntity.getStatus() == Status.VISIBLE) { // VISIBLE만 노출
                BoardDTO boardFoodDTO = new BoardDTO();
                boardFoodDTO.setId(boardFoodEntity.getId());
                boardFoodDTO.setTitle(boardFoodEntity.getTitle());
                boardFoodDTO.setBody(boardFoodEntity.getBody());
                boardFoodDTO.setImgsrc(boardFoodEntity.getImgsrc());

                if (boardFoodEntity.getAuthor() != null) {
                    boardFoodDTO.setAuthor(boardFoodEntity.getAuthor().getUsername());
                }
                UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(boardFoodEntity.getAuthor().getUsername());
                String userNickname = userInfoEntity.getNickname();
                String userGrade = userInfoService.getUserGrade(userInfoEntity.getUsername().getUsername());
                boardFoodDTO.setAuthorNickname(userNickname); // 추가
                boardFoodDTO.setGrade(userGrade);
                boardFoodDTO.setRecommend(boardFoodEntity.getRecommend());
                boardFoodDTO.setViews(boardFoodEntity.getViews());
                boardFoodDTO.setCategory(boardFoodEntity.getCategory());
                boardFoodDTO.setCreatedAt(boardFoodEntity.getCreatedAt());
                boardFoodDTO.setUpdatedAt(boardFoodEntity.getUpdatedAt());
                boardFoodDTO.setStatus(boardFoodEntity.getStatus().name());
                boardDTOList.add(boardFoodDTO);
            }
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
        UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(entity.getAuthor().getUsername());
        String userNickname = userInfoEntity.getNickname();
        return new BoardDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getBody(),
                entity.getImgsrc(),
                entity.getAuthor().getUsername(),
                userNickname,
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

        BoardFoodEntity entity = new BoardFoodEntity();
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(imgsrc);

        UserAuthEntity author = userAuthDAO.findByUsername(boardDTO.getAuthor());
        entity.setAuthor(author);

        entity.setRecommend(0);
        entity.setViews(0); // 새 글이니 조회수 0으로 시작
        entity.setCategory("food");
        entity.setStatus(Status.VISIBLE);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        BoardFoodEntity saved = boardFoodDAO.save(entity);
        userInfoDAO.updatePoint(boardDTO.getAuthor(), 3); // 3포인트증가
        return toDTO(saved);
    }

    @Transactional
    public BoardDTO updateBoard(Integer id, BoardDTO boardDTO, MultipartFile file) throws IOException {
        userInfoService.userStatusValidator(boardDTO.getAuthor());

        BoardFoodEntity entity = boardFoodDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        String imgsrc = uploadImageService.updateBoardImage(file, entity.getImgsrc()); // 이미지파일 수정
        // 원하는 필드만 수정
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(imgsrc);
        entity.setStatus(boardDTO.getStatus() != null ? Status.valueOf(boardDTO.getStatus()) : entity.getStatus());
        entity.setUpdatedAt(Instant.now());

        BoardFoodEntity updated = boardFoodDAO.save(entity);
        return toDTO(updated);
    }

    @Transactional
    public void deleteBoard(Integer id) throws IOException {
        BoardFoodEntity entity = boardFoodDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        boardFoodDAO.delete(entity);
        uploadImageService.deleteBoardImage(entity.getImgsrc()); // 이미지파일 삭제
    }

    public List<BoardDTO> findByTitle(String title) {
        List<BoardFoodEntity> boardFoodEntities=boardFoodDAO.findByTitle(title);
        return boardFoodEntities.stream()
                .map(this::toDTO)  // toDTO 적용
                .collect(Collectors.toList());
    }

    public List<BoardDTO> findByAuthor(String author) {
        List<BoardFoodEntity> boardFoodEntities=boardFoodDAO.findByAuthor(author);
        return boardFoodEntities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


//    public BoardDTO recommendBoard(Integer id) {
//        boardFoodDAO.incrementRecommend(id);
//        BoardFoodEntity entity= boardFoodDAO.findById(id)
//                .orElseThrow(() -> new RuntimeException("Board not found"));
//        return toDTO(entity);
//    }
//
//    public BoardDTO cancelRecommendBoard(Integer id) {
//        boardFoodDAO.decrementRecommend(id);
//        BoardFoodEntity entity= boardFoodDAO.findById(id)
//                .orElseThrow(() -> new RuntimeException("Board not found"));
//        return toDTO(entity);
//    }

}
