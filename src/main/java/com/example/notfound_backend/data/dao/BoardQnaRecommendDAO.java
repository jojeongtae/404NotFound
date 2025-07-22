package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.repository.BoardQnaRecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardQnaRecommendDAO {
    private final BoardQnaRecommendRepository boardQnaRecommendRepository;
}
