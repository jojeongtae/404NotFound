package com.example.notfound_backend.data.dto.pointboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizDTO {
    private Integer id;
    private String title;
    private String question;
    private String answer;
    private String imgsrc;
    private String author;
    private String authorNickname;
    private String grade;
    private Instant createdAt;
    private Integer level;
    private String category;
    private String type;
    private Integer views;

}
