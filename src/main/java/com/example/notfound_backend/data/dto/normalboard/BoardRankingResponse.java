package com.example.notfound_backend.data.dto.normalboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardRankingResponse {
    private Integer id;
    private String title;
    private String author;
    private Integer recommend;
    private Integer views;
    private String category;
    private Instant createdAt;
    private BigInteger commentCount;
    private String authorNickname;
    private String grade;
}
