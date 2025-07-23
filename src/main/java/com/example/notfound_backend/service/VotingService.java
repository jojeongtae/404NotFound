package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dao.VotingDAO;
import com.example.notfound_backend.data.dto.VotingDTO;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.data.entity.UserInfoEntity;
import com.example.notfound_backend.data.entity.UserStatus;
import com.example.notfound_backend.data.entity.VotingEntity;
import com.example.notfound_backend.exception.UserSuspendedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VotingService {
    private final VotingDAO votingDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;
    private final UserInfoService userInfoService;

    public List<VotingDTO> findAll(){
        List<VotingEntity> votingEntities=votingDAO.findAllVoting();
        List<VotingDTO> votingDTOList=new ArrayList<>();
        for(VotingEntity votingEntity:votingEntities){
            VotingDTO votingDTO=new VotingDTO();
            votingDTO.setId(votingEntity.getId());
            votingDTO.setTitle(votingEntity.getTitle());
            votingDTO.setQuestion(votingEntity.getQuestion());
            votingDTO.setAuthor(votingEntity.getAuthor().getUsername());
            UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(votingEntity.getAuthor().getUsername());
            String userNickname = userInfoEntity.getNickname();
            String userGrade = userInfoService.getUserGrade(userInfoEntity.getUsername().getUsername());
            votingDTO.setAuthorNickname(userNickname);
            votingDTO.setGrade(userGrade);
            votingDTO.setCreatedAt(votingEntity.getCreatedAt());
            votingDTO.setCategory(votingEntity.getCategory());
            votingDTO.setViews(votingEntity.getViews());
            votingDTOList.add(votingDTO);
        }
        return votingDTOList;
    }

    @Transactional
    public VotingDTO viewVoting(Integer id){
        votingDAO.incrementViews(id);
        VotingEntity entity=votingDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Voting not found"));
        return toDTO(entity);
    }

    private VotingDTO toDTO(VotingEntity entity){
        UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(entity.getAuthor().getUsername());
        String userNickname = userInfoEntity.getNickname();
        return new VotingDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getQuestion(),
                entity.getAuthor().getUsername(),
                userNickname,
                userInfoService.getUserGrade(userInfoEntity.getUsername().getUsername()),
                entity.getCreatedAt(),
                entity.getCategory(),
                entity.getViews()
        );
    }

    @Transactional
    public VotingDTO createVoting(VotingDTO dto){
        UserStatus userStatus = userInfoDAO.getUserInfo(dto.getAuthor()).getStatus();
        if (userStatus != UserStatus.ACTIVE) {
            throw new UserSuspendedException("활동 정지된 사용자입니다.");
        }
        VotingEntity entity=new VotingEntity();
        entity.setTitle(dto.getTitle());
        entity.setQuestion(dto.getQuestion());

        UserAuthEntity author=userAuthDAO.findByUsername(dto.getAuthor());
        entity.setAuthor(author);

        entity.setCreatedAt(Instant.now());
        entity.setCategory("VOTING");
        entity.setViews(0);
        VotingEntity newEntity=votingDAO.save(entity);
        return toDTO(newEntity);
    }

    @Transactional
    public VotingDTO updateVoting(Integer id, VotingDTO dto){
        UserStatus userStatus = userInfoDAO.getUserInfo(dto.getAuthor()).getStatus();
        if (userStatus != UserStatus.ACTIVE) {
            throw new UserSuspendedException("활동 정지된 사용자입니다.");
        }
        VotingEntity entity=votingDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Voting not found"));

        entity.setTitle(dto.getTitle());
        entity.setQuestion(dto.getQuestion());

        VotingEntity updated=votingDAO.save(entity);
        return toDTO(updated);
    }

    @Transactional
    public void deleteVoting(Integer id){
        VotingEntity entity=votingDAO.findById(id)
                .orElseThrow(()->new RuntimeException("Voting not found"));
        votingDAO.delete(entity);
    }



}
