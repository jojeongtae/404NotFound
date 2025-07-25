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
public class QuizResultDTO {
    private Integer id;
    private Integer quiz_id;
    private String username;
    private String userNickname;
    private String grade;
    private String userAnswer;
    private Byte result;
    private Instant solvedAt;
}
