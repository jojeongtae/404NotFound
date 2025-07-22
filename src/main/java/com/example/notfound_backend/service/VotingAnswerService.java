package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dao.UserInfoDAO;
import com.example.notfound_backend.data.dao.VotingAnswerDAO;
import com.example.notfound_backend.data.dao.VotingDAO;
import com.example.notfound_backend.data.dto.VotingAnswerDTO;
import com.example.notfound_backend.data.entity.Answers;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.data.entity.VotingAnswerEntity;
import com.example.notfound_backend.data.entity.VotingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VotingAnswerService {

    private final VotingAnswerDAO votingAnswerDAO;
    private final UserAuthDAO userAuthDAO;
    private final VotingDAO votingDAO;
    private final UserInfoDAO userInfoDAO;

    @Transactional
    public VotingAnswerDTO submitAnswer(VotingAnswerDTO dto) {
        VotingAnswerEntity entity = new VotingAnswerEntity();

        if (dto.getUsername() != null) {
            UserAuthEntity user = userAuthDAO.findByUsername(dto.getUsername());
            entity.setUser(user);
        }

        String ansStr = dto.getAnswers().trim();
        Answers answerEnum = Answers.valueOf(ansStr);
        entity.setAnswers(answerEnum);
        entity.setCreatedAt(Instant.now());

        VotingEntity parentVoting = votingDAO.findById(dto.getParent())
                .orElseThrow(() -> new IllegalArgumentException("투표 ID가 유효하지 않습니다: "));
        entity.setParent(parentVoting);

        VotingAnswerEntity saved = votingAnswerDAO.save(entity);
        return toDTO(saved);
    }

    public List<VotingAnswerDTO> getAnswersByVoting(int parentId){
        return votingAnswerDAO.findByParentId(parentId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private VotingAnswerDTO toDTO(VotingAnswerEntity entity) {
        return VotingAnswerDTO.builder()
                .id(entity.getId())
                .username(entity.getUser().getUsername())
                .answers(entity.getAnswers().toString())
                .userNickname(userInfoDAO.getUserInfo(entity.getUser().getUsername()).getNickname())
                .reason(entity.getReason())
                .createdAt(entity.getCreatedAt())
                .parent(entity.getParent().getId())
                .build();
    }

}
