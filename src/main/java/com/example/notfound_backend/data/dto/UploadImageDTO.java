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
public class UploadImageDTO {
    private Integer id;
    private String originalName;
    private String savedName;
    private String filePath;
    private Instant uploadedAt;
    private Long fileSize; // byte
}
