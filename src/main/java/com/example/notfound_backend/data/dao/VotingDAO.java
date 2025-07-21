package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.VotingEntity;
import com.example.notfound_backend.data.repository.VotingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VotingDAO {
    private final VotingRepository votingRepository;

    public List<VotingEntity> findAllVoting() {
        return votingRepository.findAll();
    }

    public VotingEntity save(VotingEntity votingEntity) {
        return votingRepository.save(votingEntity);
    }

    public void incrementViews(Integer id){
        votingRepository.incrementViews(id);
    }

    public Optional<VotingEntity> findById(Integer id){
        return votingRepository.findById(id);
    }

    public void delete(VotingEntity votingEntity){
        votingRepository.delete(votingEntity);
    }
}
