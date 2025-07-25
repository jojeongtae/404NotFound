package com.example.notfound_backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportAddDTO {
    private String reason; // 신고사유(간단유형)
    private String description; // 상세설명(선택)
    private String reporter; // 신고자username
    private String reported; // 피신고자username
    private String targetTable; // 신고 글 테이블명
    private Integer targetId; // 신고 글 ID
}
