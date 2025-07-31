package com.example.notfound_backend.data.dto.normalboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardCommentDTO {
    private Integer id;
    private Integer boardId;
    private Integer parentId;
    private String author;
    private String authorNickname;
    private String content;
    private Instant createdAt;
    private String grade;
    private String status;
}
