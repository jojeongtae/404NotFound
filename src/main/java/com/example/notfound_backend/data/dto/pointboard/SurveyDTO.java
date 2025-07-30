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
public class SurveyDTO {
    private Integer id;
    private String title;
    private String question;
    private String column1;
    private String column2;
    private String column3;
    private String column4;
    private String column5;
    private String imgsrc;
    private String author;
    private String authorNickname;
    private String grade;
    private Instant createdAt;
    private String category;
    private Integer views;
}
