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
public class SurveyAnswerDTO {
    private Integer id;
    private String username;
    private String userNickname;
    private String answers;
    private Instant createdAt;
    private Integer parentId;
}
