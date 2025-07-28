package com.example.notfound_backend.service.pointboard;

import com.example.notfound_backend.data.dao.login.UserAuthDAO;
import com.example.notfound_backend.data.dao.admin.UserInfoDAO;
import com.example.notfound_backend.data.dao.pointboard.VotingAnswerDAO;
import com.example.notfound_backend.data.dao.pointboard.VotingDAO;
import com.example.notfound_backend.data.dto.pointboard.VotingAnswerDTO;
import com.example.notfound_backend.data.entity.admin.UserInfoEntity;
import com.example.notfound_backend.data.entity.enumlist.Answers;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.pointboard.VotingAnswerEntity;
import com.example.notfound_backend.data.entity.pointboard.VotingEntity;
import com.example.notfound_backend.service.admin.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VotingAnswerService {

    private final VotingAnswerDAO votingAnswerDAO;
    private final UserAuthDAO userAuthDAO;
    private final VotingDAO votingDAO;
    private final UserInfoDAO userInfoDAO;
    private final UserInfoService userInfoService;

    @Transactional
    public VotingAnswerDTO submitAnswer(VotingAnswerDTO dto) {
        userInfoService.userStatusValidator(dto.getUsername());

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
        UserInfoEntity userInfoEntity = userInfoDAO.getUserInfo(entity.getUser().getUsername());
        return VotingAnswerDTO.builder()
                .id(entity.getId())
                .username(entity.getUser().getUsername())
                .answers(entity.getAnswers().toString())
                .userNickname(userInfoEntity.getNickname())
                .reason(entity.getReason())
                .createdAt(entity.getCreatedAt())
                .parent(entity.getParent().getId())
                .grade(userInfoService.getUserGrade(userInfoEntity.getUsername().getUsername()))
                .build();
    }

}
