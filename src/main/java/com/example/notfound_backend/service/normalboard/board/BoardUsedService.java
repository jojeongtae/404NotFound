package com.example.notfound_backend.service.normalboard.board;

import com.example.notfound_backend.data.dao.normalboard.board.BoardUsedDAO;
import com.example.notfound_backend.data.dao.login.UserAuthDAO;
import com.example.notfound_backend.data.dao.admin.UserInfoDAO;
import com.example.notfound_backend.data.dto.admin.UserInfoAllDTO;
import com.example.notfound_backend.data.dto.normalboard.BoardRankingDTO;
import com.example.notfound_backend.data.dto.normalboard.BoardRankingResponse;
import com.example.notfound_backend.data.dto.normalboard.BoardUsedDTO;
import com.example.notfound_backend.data.entity.admin.UserInfoEntity;
import com.example.notfound_backend.data.entity.enumlist.Status;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.normalboard.board.BoardUsedEntity;
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
public class BoardUsedService {

    private final BoardUsedDAO boardUsedDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;
    private final UserInfoService userInfoService;
    private final UploadImageService uploadImageService;

    // 외부인용 (VISIBLE만 조회)
    public List<BoardUsedDTO> findAll() {
        List<BoardUsedEntity> entityList = boardUsedDAO.findAllByStatus(Status.VISIBLE);
        List<BoardUsedDTO> boardUsedDTOList = new ArrayList<>();
        for(BoardUsedEntity entity : entityList){
            boardUsedDTOList.add(convertToBoardUsedDTO(entity));
        }
        return boardUsedDTOList;
    }

    // 유저용 (VISIBLE + 자신의 PRIVATE 조회)
    public List<BoardUsedDTO> findAllByUser(String username) {
        List<BoardUsedEntity> entityList = boardUsedDAO.findAllBoardsByUser(username);
        List<BoardUsedDTO> boardUsedDTOList = new ArrayList<>();
        for(BoardUsedEntity entity : entityList){
            boardUsedDTOList.add(convertToBoardUsedDTO(entity));
        }
        return boardUsedDTOList;
    }

    // 관리자용 (모든상태 게시글 조회)
    public List<BoardUsedDTO> findAllByAdmin(String username) {
        if(!userAuthDAO.isAdmin(username)) {
            throw new UnauthorizedAccessException("관리자만 접근가능합니다.");
        }
        List<BoardUsedEntity> entityList = boardUsedDAO.findAllBoards();
        List<BoardUsedDTO> boardUsedDTOList = new ArrayList<>();
        for(BoardUsedEntity entity : entityList) {
            boardUsedDTOList.add(convertToBoardUsedDTO(entity));
        }
        return boardUsedDTOList;
    }

    private BoardUsedDTO convertToBoardUsedDTO(BoardUsedEntity entity) {
        UserInfoAllDTO userInfo = userInfoService.getUserInfo(entity.getAuthor().getUsername());
        return BoardUsedDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .body(entity.getBody())
                .imgsrc(entity.getImgsrc())
                .price(entity.getPrice())
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
    public BoardUsedDTO viewBoard(Integer id) {
        boardUsedDAO.incrementViews(id);
        BoardUsedEntity entity= boardUsedDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Board not found"));
        return toDTO(entity);
    }

    private BoardUsedDTO toDTO(BoardUsedEntity entity) {
        UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(entity.getAuthor().getUsername());
        String userNickname = userInfoEntity.getNickname();
        return new BoardUsedDTO(
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
    public BoardUsedDTO createBoard(BoardUsedDTO
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
    public BoardUsedDTO updateBoard(Integer id, BoardUsedDTO boardDTO, MultipartFile file) throws IOException {
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

    public List<BoardUsedDTO> findByTitle(String title) {
        List<BoardUsedEntity> boardUsedEntities=boardUsedDAO.findByTitle(title);
        return boardUsedEntities.stream()
                .map(this::toDTO)  // toDTO 적용
                .collect(Collectors.toList());
    }

    public List<BoardUsedDTO> findByAuthor(String author) {
        List<BoardUsedEntity> boardUsedEntities=boardUsedDAO.findByAuthor(author);
        return boardUsedEntities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 게시판 상태변경 (본인, 관리자)
    @Transactional
    public BoardUsedDTO updateBoardStatus(Integer id, BoardUsedDTO boardUsedDTO) { // boardUsedDTO(author,status)
        userInfoService.userStatusValidator(boardUsedDTO.getAuthor());

        BoardUsedEntity entity = boardUsedDAO.findById(id).orElseThrow(()->new RuntimeException("Board not found"));

        String author = boardUsedDTO.getAuthor();
        if (author == null || (!author.equals(entity.getAuthor().getUsername()) && !userAuthDAO.getRole(author).equals("ROLE_ADMIN"))) { // 본인아니고, admin도 아니면
            throw new UnauthorizedAccessException("해당 게시글을 수정할 권한이 없습니다. 작성자 또는 관리자만 수정 가능");
        }
        try {
            entity.setStatus(Status.valueOf(boardUsedDTO.getStatus()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("올바르지 않은 상태 값입니다: " + boardUsedDTO.getStatus());
        }
        entity.setUpdatedAt(Instant.now());
        BoardUsedEntity saved = boardUsedDAO.save(entity);
        return toDTO(saved);
    }

    public List<BoardRankingResponse> getUsedTop5ByRecommendInLast7Days(){
        List<BoardRankingDTO> dtos = boardUsedDAO.getTop5ByRecommend();

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
