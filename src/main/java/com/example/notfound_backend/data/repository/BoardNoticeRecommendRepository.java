package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.BoardNoticeRecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardNoticeRecommendRepository extends JpaRepository<BoardNoticeRecommendEntity, Integer> {
}
