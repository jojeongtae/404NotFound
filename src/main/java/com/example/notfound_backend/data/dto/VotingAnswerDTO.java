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
public class VotingAnswerDTO {
    private Integer id;
    private String username;
    private String answers;
    private String reason;
    private Instant createdAt;
    private Integer parent;
}
