package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.VotingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VotingRepository extends JpaRepository<VotingEntity, Integer> {

//    @Query("SELECT v FROM VotingEntity v WHERE v.author.username = :username")
//    Optional<VotingEntity> findByAuthorUsername(@Param("username") String username);

    List<VotingEntity> findAll();

    @Modifying
    @Query("UPDATE VotingEntity b SET b.views=b.views+1 WHERE b.id=:id")
    void incrementViews(@Param("id") Integer id);

}
