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
public class VotingAnswerDTO {
    private Integer id;
    private String username;
    private String userNickname;
    private String grade;
    private String answers;
    private String reason;
    private Instant createdAt;
    private Integer parent;
}
