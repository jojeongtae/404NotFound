package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.repository.BoardInfoRecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardInfoRecommendDAO {
    private final BoardInfoRecommendRepository boardInfoRecommendRepository;
}
