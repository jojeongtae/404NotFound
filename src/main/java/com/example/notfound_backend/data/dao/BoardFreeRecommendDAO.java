package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.repository.BoardFreeRecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardFreeRecommendDAO {
    private final BoardFreeRecommendRepository boardFreeRecommendRepository;
}
