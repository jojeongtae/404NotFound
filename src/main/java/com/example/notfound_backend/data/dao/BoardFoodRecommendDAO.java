package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.repository.BoardFoodRecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardFoodRecommendDAO {
    private final BoardFoodRecommendRepository boardFoodRecommendRepository;
}
