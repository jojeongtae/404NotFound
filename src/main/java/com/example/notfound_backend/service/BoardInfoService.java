package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardInfoDAO;
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
public class BoardInfoService {

    private final BoardInfoDAO boardInfoDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;
    private final UserInfoService userInfoService;
    private final UploadImageService uploadImageService;

    public List<BoardDTO> findAll() {
        List<BoardInfoEntity> boardInfoEntityList = boardInfoDAO.findAllBoards();
        List<BoardDTO> boardDTOList =new ArrayList<>();
        for(BoardInfoEntity boardInfoEntity : boardInfoEntityList){
            if (boardInfoEntity.getStatus() == Status.VISIBLE) { // VISIBLE만 노출
                BoardDTO boardInfoDTO = new BoardDTO();
                boardInfoDTO.setId(boardInfoEntity.getId());
                boardInfoDTO.setTitle(boardInfoEntity.getTitle());
                boardInfoDTO.setBody(boardInfoEntity.getBody());
                boardInfoDTO.setImgsrc(boardInfoEntity.getImgsrc());

                if (boardInfoEntity.getAuthor() != null) {
                    boardInfoDTO.setAuthor(boardInfoEntity.getAuthor().getUsername());
                }
                UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(boardInfoEntity.getAuthor().getUsername());
                String userNickname = userInfoEntity.getNickname();
                String userGrade = userInfoService.getUserGrade(userInfoEntity.getUsername().getUsername());
                boardInfoDTO.setAuthorNickname(userNickname); // 추가
                boardInfoDTO.setGrade(userGrade);
                boardInfoDTO.setRecommend(boardInfoEntity.getRecommend());
                boardInfoDTO.setViews(boardInfoEntity.getViews());
                boardInfoDTO.setCategory(boardInfoEntity.getCategory());
                boardInfoDTO.setCreatedAt(boardInfoEntity.getCreatedAt());
                boardInfoDTO.setUpdatedAt(boardInfoEntity.getUpdatedAt());
                boardInfoDTO.setStatus(boardInfoEntity.getStatus().name());
                boardDTOList.add(boardInfoDTO);
            }
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

        BoardInfoEntity entity = new BoardInfoEntity();
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(imgsrc);

        UserAuthEntity author = userAuthDAO.findByUsername(boardDTO.getAuthor());
        entity.setAuthor(author);

        entity.setRecommend(0);
        entity.setViews(0); // 새 글이니 조회수 0으로 시작
        entity.setCategory("info");
        entity.setStatus(Status.VISIBLE);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        BoardInfoEntity saved = boardInfoDAO.save(entity);
        userInfoDAO.updatePoint(boardDTO.getAuthor(), 3); // 3포인트증가
        return toDTO(saved);
    }

    @Transactional
    public BoardDTO updateBoard(Integer id, BoardDTO boardDTO, MultipartFile file) throws IOException {
        userInfoService.userStatusValidator(boardDTO.getAuthor());

        BoardInfoEntity entity = boardInfoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        String imgsrc = uploadImageService.updateBoardImage(file, entity.getImgsrc()); // 이미지파일 수정

        // 원하는 필드만 수정
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(imgsrc);
        entity.setStatus(boardDTO.getStatus() != null ? Status.valueOf(boardDTO.getStatus()) : entity.getStatus());
        entity.setUpdatedAt(Instant.now());

        BoardInfoEntity updated = boardInfoDAO.save(entity);
        return toDTO(updated);
    }

    @Transactional
    public void deleteBoard(Integer id) throws IOException {
        BoardInfoEntity entity = boardInfoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        boardInfoDAO.delete(entity);
        uploadImageService.deleteBoardImage(entity.getImgsrc()); // 이미지파일 삭제
    }

    public List<BoardDTO> findByTitle(String title){
        List<BoardInfoEntity> boardInfoEntityList = boardInfoDAO.findByTitle(title);
        return boardInfoEntityList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<BoardDTO> findByAuthor(String author){
        List<BoardInfoEntity> boardInfoEntityList = boardInfoDAO.findByAuthor(author);
        return boardInfoEntityList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

//    public BoardDTO recommendBoard(Integer id) {
//        boardInfoDAO.incrementRecommend(id);
//        BoardInfoEntity entity= boardInfoDAO.findById(id)
//                .orElseThrow(() -> new RuntimeException("Board not found"));
//        return toDTO(entity);
//    }
//
//    public BoardDTO cancelRecommendBoard(Integer id) {
//        boardInfoDAO.decrementRecommend(id);
//        BoardInfoEntity entity= boardInfoDAO.findById(id)
//                .orElseThrow(() -> new RuntimeException("Board not found"));
//        return toDTO(entity);
//    }

}
