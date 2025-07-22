package com.example.notfound_backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDTO {
    private Integer id;
    private String title;
    private String body;
    private String imgsrc;
    private String author; //username
    private String authorNickname; // 추가
    private Integer recommend;
    private Integer views;
    private String category;
    private Instant createdAt;
    private Instant updatedAt;
    private String status;
}
