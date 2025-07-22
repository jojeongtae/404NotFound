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
public class BoardRecommendDTO {
    private Integer id;
    private Integer boardId;
    private String username;
    private Byte isActive;
    private Instant createdAt;
    private Instant updatedAt;
}
