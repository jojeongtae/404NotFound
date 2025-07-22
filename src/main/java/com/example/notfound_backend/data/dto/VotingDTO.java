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
public class VotingDTO {
    private Integer id;
    private String title;
    private String question;
    private String author;
    private String authorNickname;
    private String grade;
    private Instant createdAt;
    private String category;
    private Integer views;
}
