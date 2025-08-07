package com.example.notfound_backend.data.dto.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {
    private Integer id;
    private String title;
    private String message;
    private String author;
    private String authorNickname;
    private String receiver;
    private String receiverNickname;
    private Instant createdAt;
}
