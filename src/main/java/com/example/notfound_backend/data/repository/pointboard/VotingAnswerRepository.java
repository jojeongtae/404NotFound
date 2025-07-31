package com.example.notfound_backend.data.repository.pointboard;

import com.example.notfound_backend.data.entity.pointboard.VotingAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotingAnswerRepository extends JpaRepository<VotingAnswerEntity, Integer> {
//    Optional<VotingAnswerEntity> findVotingAnswerByParent_Id(Integer id);

    List<VotingAnswerEntity> findByParentId(Integer parentId);

    List<VotingAnswerEntity> findByUser_Username(String username);
}
