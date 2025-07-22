package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.repository.BoardNoticeRecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardNoticeRecommendDAO {
    private final BoardNoticeRecommendRepository boardNoticeRecommendRepository;
}
