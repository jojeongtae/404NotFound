package com.example.notfound_backend.data.dto;

import com.example.notfound_backend.data.entity.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportResponseDTO {
    private Integer id;
    private String reason; // 신고사유(간단유형)
    private String description; // 상세설명(선택)
    private String reporter; // 신고자username
    private String reported; // 피신고자username
    private String targetTable; // 신고 글 테이블명
    private Integer targetId; // 신고 글 ID
    private ReportStatus status; // PENDING,ACCEPTED,REJECTED,
    private LocalDateTime createdAt; // 신고 시간
    private LocalDateTime updatedAt; // 처리 시간
}
