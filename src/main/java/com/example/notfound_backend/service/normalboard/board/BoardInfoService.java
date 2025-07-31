package com.example.notfound_backend.service.normalboard.board;

import com.example.notfound_backend.data.dao.normalboard.board.BoardInfoDAO;
import com.example.notfound_backend.data.dao.login.UserAuthDAO;
import com.example.notfound_backend.data.dao.admin.UserInfoDAO;
import com.example.notfound_backend.data.dto.admin.UserInfoAllDTO;
import com.example.notfound_backend.data.dto.normalboard.BoardDTO;
import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
import com.example.notfound_backend.data.dto.normalboard.BoardRankingResponse;
import com.example.notfound_backend.data.entity.admin.UserInfoEntity;
import com.example.notfound_backend.data.entity.enumlist.Status;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.normalboard.board.BoardInfoEntity;
import com.example.notfound_backend.exception.UnauthorizedAccessException;
import com.example.notfound_backend.service.utility.UploadImageService;
import com.example.notfound_backend.service.admin.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
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
        List<BoardInfoEntity> entityList = boardInfoDAO.findAllByStatus(Status.VISIBLE);
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardInfoEntity entity : entityList){
            boardDTOList.add(convertToBoardDTO(entity));
        }
        return boardDTOList;
    }
    // 유저용 (VISIBLE + 자신의 PRIVATE 조회)
    public List<BoardDTO> findAllByUser(String username) {
        List<BoardInfoEntity> entityList = boardInfoDAO.findAllBoardsByUser(username);
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardInfoEntity entity : entityList){
            boardDTOList.add(convertToBoardDTO(entity));
        }
        return boardDTOList;
    }

    // 관리자용 (모든상태 게시글 조회)
    public List<BoardDTO> findAllByAdmin(String username) {
        if(!userAuthDAO.isAdmin(username)) {
            throw new UnauthorizedAccessException("관리자만 접근가능합니다.");
        }
        List<BoardInfoEntity> entityList = boardInfoDAO.findAllBoards();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardInfoEntity entity : entityList) {
            boardDTOList.add(convertToBoardDTO(entity));
        }
        return boardDTOList;
    }

    private BoardDTO convertToBoardDTO(BoardInfoEntity entity) {
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

    // 게시판 상태변경 (본인, 관리자)
    @Transactional
    public BoardDTO updateBoardStatus(Integer id, BoardDTO boardDTO) { // boardDTO(author,status)
        userInfoService.userStatusValidator(boardDTO.getAuthor());

        BoardInfoEntity entity = boardInfoDAO.findById(id).orElseThrow(()->new RuntimeException("Board not found"));

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
        BoardInfoEntity saved = boardInfoDAO.save(entity);
        return toDTO(saved);
    }

    public List<BoardRankingResponse> getInfoTop5ByRecommendInLast7Days(){
        List<BoardRankingDTO> dtos = boardInfoDAO.getTop5ByRecommend();

        List<BoardRankingResponse> result = dtos.stream()
                .map(dto -> new BoardRankingResponse(
                        dto.getId(),
                        dto.getTitle(),
                        dto.getAuthor(),
                        dto.getRecommend(),
                        dto.getViews(),
                        dto.getCategory(),
                        dto.getCreatedAt(),
                        dto.getCommentCount(),
                        dto.getAuthorNickname(),
                        userInfoService.getUserGrade(dto.getAuthor())
                ))
                .sorted(Comparator.comparing(BoardRankingResponse::getRecommend,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(5)
                .collect(Collectors.toList());

        return result;
    }

}
