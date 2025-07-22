package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.repository.BoardUsedRecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardUsedRecommendDAO {
    private final BoardUsedRecommendRepository boardUsedRecommendRepository;

    @Transactional
    public void incrementRecommend(Integer id){

        boardUsedRecommendRepository.incrementRecommend(id);
    }
}
