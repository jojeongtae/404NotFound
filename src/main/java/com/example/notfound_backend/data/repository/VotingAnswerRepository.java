package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.VotingAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VotingAnswerRepository extends JpaRepository<VotingAnswerEntity, Integer> {
//    Optional<VotingAnswerEntity> findVotingAnswerByParent_Id(Integer id);

    List<VotingAnswerEntity> findByParent_Id(Integer parentId);

    List<VotingAnswerEntity> findByUser_Username(String username);
}
