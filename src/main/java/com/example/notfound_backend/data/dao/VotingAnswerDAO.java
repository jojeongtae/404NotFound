package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.VotingAnswerEntity;
import com.example.notfound_backend.data.repository.VotingAnswerRepository;
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
