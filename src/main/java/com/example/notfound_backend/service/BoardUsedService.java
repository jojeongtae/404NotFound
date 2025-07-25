package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardUsedDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dto.BoardUsedDTO;
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
public class BoardUsedService {

    private final BoardUsedDAO boardUsedDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;
    private final UserInfoService userInfoService;
    private final UploadImageService uploadImageService;

    public List<BoardUsedDTO> findAll() {
        List<BoardUsedEntity> boardUsedEntityList = boardUsedDAO.findAllBoards();
        List<BoardUsedDTO> boardDTOList =new ArrayList<>();
        for(BoardUsedEntity boardUsedEntity : boardUsedEntityList) {
            if (boardUsedEntity.getStatus() == Status.VISIBLE) { // VISIBLE만 노출
                BoardUsedDTO
                        boardUsedDTO = new BoardUsedDTO
                        ();
                boardUsedDTO.setId(boardUsedEntity.getId());
                boardUsedDTO.setTitle(boardUsedEntity.getTitle());
                boardUsedDTO.setBody(boardUsedEntity.getBody());
                boardUsedDTO.setImgsrc(boardUsedEntity.getImgsrc());
                boardUsedDTO.setPrice(boardUsedEntity.getPrice());

                if (boardUsedEntity.getAuthor() != null) {
                    boardUsedDTO.setAuthor(boardUsedEntity.getAuthor().getUsername());
                }
                UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(boardUsedEntity.getAuthor().getUsername());
                String userNickname = userInfoEntity.getNickname();
                String userGrade = userInfoService.getUserGrade(userInfoEntity.getUsername().getUsername());
                boardUsedDTO.setAuthorNickname(userNickname); // 추가
                boardUsedDTO.setGrade(userGrade);
                boardUsedDTO.setRecommend(boardUsedEntity.getRecommend());
                boardUsedDTO.setViews(boardUsedEntity.getViews());
                boardUsedDTO.setCategory(boardUsedEntity.getCategory());
                boardUsedDTO.setCreatedAt(boardUsedEntity.getCreatedAt());
                boardUsedDTO.setUpdatedAt(boardUsedEntity.getUpdatedAt());
                boardUsedDTO.setStatus(boardUsedEntity.getStatus().name());
                boardDTOList.add(boardUsedDTO);
            }
        }
        return boardDTOList;
    }

    @Transactional
    public BoardUsedDTO
    viewBoard(Integer id) {
        boardUsedDAO.incrementViews(id);
        BoardUsedEntity entity= boardUsedDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Board not found"));
        return toDTO(entity);
    }

    private BoardUsedDTO
    toDTO(BoardUsedEntity entity) {
        UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(entity.getAuthor().getUsername());
        String userNickname = userInfoEntity.getNickname();
        return new BoardUsedDTO
                (
                entity.getId(),
                entity.getTitle(),
                entity.getBody(),
                entity.getImgsrc(),
                entity.getPrice(),
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
    public BoardUsedDTO
    createBoard(BoardUsedDTO
                        boardDTO, MultipartFile file) throws IOException {
        userInfoService.userStatusValidator(boardDTO.getAuthor());
        String imgsrc = uploadImageService.uploadBoardImage(file); // 이미지파일 저장

        BoardUsedEntity entity = new BoardUsedEntity();
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(imgsrc);
        entity.setPrice(boardDTO.getPrice());

        UserAuthEntity author = userAuthDAO.findByUsername(boardDTO.getAuthor());
        entity.setAuthor(author);

        entity.setRecommend(0);
        entity.setViews(0); // 새 글이니 조회수 0으로 시작
        entity.setCategory("used");
        entity.setStatus(Status.VISIBLE);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        BoardUsedEntity saved = boardUsedDAO.save(entity);
        userInfoDAO.updatePoint(boardDTO.getAuthor(), 3); // 3포인트증가
        return toDTO(saved);
    }

    @Transactional
    public BoardUsedDTO
    updateBoard(Integer id, BoardUsedDTO
            boardDTO, MultipartFile file) throws IOException {
        userInfoService.userStatusValidator(boardDTO.getAuthor());

        BoardUsedEntity entity = boardUsedDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        String imgsrc = uploadImageService.updateBoardImage(file, entity.getImgsrc()); // 이미지파일 수정
        // 원하는 필드만 수정
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(imgsrc);
        entity.setPrice(boardDTO.getPrice());
        entity.setStatus(boardDTO.getStatus() != null ? Status.valueOf(boardDTO.getStatus()) : entity.getStatus());
        entity.setUpdatedAt(Instant.now());

        BoardUsedEntity updated = boardUsedDAO.save(entity);
        return toDTO(updated);
    }

    @Transactional
    public void deleteBoard(Integer id) throws IOException {
        BoardUsedEntity entity = boardUsedDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        boardUsedDAO.delete(entity);
        uploadImageService.deleteBoardImage(entity.getImgsrc()); // 이미지파일 삭제
    }

    public List<BoardUsedDTO
            > findByTitle(String title) {
        List<BoardUsedEntity> boardUsedEntities=boardUsedDAO.findByTitle(title);
        return boardUsedEntities.stream()
                .map(this::toDTO)  // toDTO 적용
                .collect(Collectors.toList());
    }

    public List<BoardUsedDTO
            > findByAuthor(String author) {
        List<BoardUsedEntity> boardUsedEntities=boardUsedDAO.findByAuthor(author);
        return boardUsedEntities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


//    public BoardUsedDTO
//    recommendBoard(Integer id) {
//        boardUsedDAO.incrementRecommend(id);
//        BoardUsedEntity entity= boardUsedDAO.findById(id)
//                .orElseThrow(() -> new RuntimeException("Board not found"));
//        return toDTO(entity);
//    }
//
//    public BoardUsedDTO
//    cancelRecommendBoard(Integer id) {
//        boardUsedDAO.decrementRecommend(id);
//        BoardUsedEntity entity= boardUsedDAO.findById(id)
//                .orElseThrow(() -> new RuntimeException("Board not found"));
//        return toDTO(entity);
//    }

}
