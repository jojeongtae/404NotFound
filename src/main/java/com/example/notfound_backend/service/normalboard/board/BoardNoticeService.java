package com.example.notfound_backend.service.normalboard.board;

import com.example.notfound_backend.data.dao.normalboard.board.BoardNoticeDAO;
import com.example.notfound_backend.data.dao.login.UserAuthDAO;
import com.example.notfound_backend.data.dao.admin.UserInfoDAO;
import com.example.notfound_backend.data.dto.admin.UserInfoAllDTO;
import com.example.notfound_backend.data.dto.normalboard.BoardDTO;
import com.example.notfound_backend.data.entity.admin.UserInfoEntity;
import com.example.notfound_backend.data.entity.enumlist.Status;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.normalboard.board.BoardNoticeEntity;
import com.example.notfound_backend.exception.UnauthorizedAccessException;
import com.example.notfound_backend.service.utility.UploadImageService;
import com.example.notfound_backend.service.admin.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
public class BoardNoticeService {
    private final BoardNoticeDAO boardNoticeDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;
    private final UserInfoService userInfoService;
    private final UploadImageService uploadImageService;

    // 외부인용 (VISIBLE만 조회)
    public List<BoardDTO> findAll() {
        List<BoardNoticeEntity> entityList = boardNoticeDAO.findAllByStatus(Status.VISIBLE);
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardNoticeEntity entity : entityList){
            boardDTOList.add(convertToBoardDTO(entity));
        }
        return boardDTOList;
    }

    // 관리자용 (모든상태 게시글 조회)
    public List<BoardDTO> findAllByAdmin(String username) {
        if(!userAuthDAO.isAdmin(username)) {
            throw new UnauthorizedAccessException("관리자만 접근가능합니다.");
        }
        List<BoardNoticeEntity> entityList = boardNoticeDAO.findAllBoards();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardNoticeEntity entity : entityList) {
            boardDTOList.add(convertToBoardDTO(entity));
        }
        return boardDTOList;
    }

    private BoardDTO convertToBoardDTO(BoardNoticeEntity entity) {
        UserInfoAllDTO userInfo = userInfoService.getUserInfo(entity.getAuthor().getUsername());
        return BoardDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .body(entity.getBody())
                .imgsrc(entity.getImgsrc())
                .author(entity.getAuthor().getUsername())
                .authorNickname(userInfo.getNickname())
                .grade(userInfo.getGrade())
                .recommend(entity.getRecommend())
                .views(entity.getViews())
                .category(entity.getCategory())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .status(entity.getStatus().name())
                .build();
    }

    @Transactional
    public BoardDTO viewBoard(Integer id) {
        boardNoticeDAO.incrementViews(id);
        BoardNoticeEntity entity= boardNoticeDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Board not found"));
        return toDTO(entity);
    }

    private BoardDTO toDTO(BoardNoticeEntity entity) {
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
        UserAuthEntity author = userAuthDAO.findByUsername(boardDTO.getAuthor());
        if (!"ROLE_ADMIN".equals(author.getRole())) {
            throw new AccessDeniedException("관리자만 글을 작성할 수 있습니다.");
        }
        String imgsrc = uploadImageService.uploadBoardImage(file); // 이미지파일 저장

        BoardNoticeEntity entity = new BoardNoticeEntity();
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(imgsrc);
        entity.setAuthor(author);
        entity.setRecommend(0);
        entity.setViews(0);
        entity.setCategory("notice");
        entity.setStatus(Status.VISIBLE);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        BoardNoticeEntity saved = boardNoticeDAO.save(entity);
        userInfoDAO.updatePoint(boardDTO.getAuthor(), 3); // 3포인트증가
        return toDTO(saved);
    }

    @Transactional
    public BoardDTO updateBoard(Integer id, BoardDTO boardDTO, MultipartFile file) throws IOException {

        BoardNoticeEntity entity = boardNoticeDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        UserAuthEntity author = userAuthDAO.findByUsername(boardDTO.getAuthor());
        if (!"ROLE_ADMIN".equals(author.getRole())) {
            throw new AccessDeniedException("관리자만 글을 작성할 수 있습니다.");
        }
        String imgsrc = uploadImageService.updateBoardImage(file, entity.getImgsrc()); // 이미지파일 수정
        // 원하는 필드만 수정
        entity.setTitle(boardDTO.getTitle());
        entity.setBody(boardDTO.getBody());
        entity.setImgsrc(imgsrc);
        entity.setStatus(boardDTO.getStatus() != null ? Status.valueOf(boardDTO.getStatus()) : entity.getStatus());
        entity.setUpdatedAt(Instant.now());

        BoardNoticeEntity updated = boardNoticeDAO.save(entity);
        return toDTO(updated);
    }


    @Transactional
    public void deleteBoard(Integer id, String username) throws IOException {

        BoardNoticeEntity entity = boardNoticeDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        UserAuthEntity user = userAuthDAO.findByUsername(username);
        if (user == null || !user.getRole().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("관리자만 삭제할 수 있습니다.");
        }
        boardNoticeDAO.delete(entity);
        uploadImageService.deleteBoardImage(entity.getImgsrc()); // 이미지파일 삭제
    }

    public List<BoardDTO> findByTitle(String title) {
        List<BoardNoticeEntity> boardNoticeEntities=boardNoticeDAO.findByTitle(title);
        return boardNoticeEntities.stream()
                .map(this::toDTO)  // toDTO 적용
                .collect(Collectors.toList());
    }

    public List<BoardDTO> findByAuthor(String author) {
        List<BoardNoticeEntity> boardNoticeEntities=boardNoticeDAO.findByAuthor(author);
        return boardNoticeEntities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 게시판 상태변경 (본인, 관리자)
    @Transactional
    public BoardDTO updateBoardStatus(Integer id, BoardDTO boardDTO) { // boardDTO(author,status)
        userInfoService.userStatusValidator(boardDTO.getAuthor());

        BoardNoticeEntity entity = boardNoticeDAO.findById(id).orElseThrow(()->new RuntimeException("Board not found"));

        String author = boardDTO.getAuthor();
        if (author == null || (!author.equals(entity.getAuthor().getUsername()) && !userAuthDAO.getRole(author).equals("ROLE_ADMIN"))) { // 본인아니고, admin도 아니면
            throw new UnauthorizedAccessException("해당 게시글을 수정할 권한이 없습니다. 작성자 또는 관리자만 수정 가능");
        }
        try {
            entity.setStatus(Status.valueOf(boardDTO.getStatus()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("올바르지 않은 상태 값입니다: " + boardDTO.getStatus());
        }
        entity.setUpdatedAt(Instant.now());
        BoardNoticeEntity saved = boardNoticeDAO.save(entity);
        return toDTO(saved);
    }

//    public BoardDTO recommendBoard(Integer id) {
//        boardNoticeDAO.incrementRecommend(id);
//        BoardNoticeEntity entity= boardNoticeDAO.findById(id)
//                .orElseThrow(() -> new RuntimeException("Board not found"));
//        return toDTO(entity);
//    }
//
//    public BoardDTO cancelRecommendBoard(Integer id) {
//        boardNoticeDAO.decrementRecommend(id);
//        BoardNoticeEntity entity= boardNoticeDAO.findById(id)
//                .orElseThrow(() -> new RuntimeException("Board not found"));
//        return toDTO(entity);
//    }

}
