package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.BoardUsedRecommendDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardUsedRecommendService {
    private final BoardUsedRecommendDAO  boardUsedRecommendDAO;


}
