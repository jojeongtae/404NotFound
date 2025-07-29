package com.example.notfound_backend.data.dao.pointboard;

import com.example.notfound_backend.data.entity.pointboard.VotingAnswerEntity;
import com.example.notfound_backend.data.repository.pointboard.VotingAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VotingAnswerDAO {
    private final VotingAnswerRepository votingAnswerRepository;

    public VotingAnswerEntity save(VotingAnswerEntity entity) {
        return votingAnswerRepository.save(entity);
    }

    public List<VotingAnswerEntity> findByParentId(Integer parentId) {
        return votingAnswerRepository.findByParentId(parentId);
    }

    public List<VotingAnswerEntity> findByUsername(String username) {
        return votingAnswerRepository.findByUser_Username(username);
    }
}
