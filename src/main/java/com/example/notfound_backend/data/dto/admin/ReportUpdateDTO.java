package com.example.notfound_backend.data.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportUpdateDTO {
    private Integer reportId;
    private String reason; // 신고사유(간단유형)
    private String description; // 상세설명(선택)
    private String reporter; // 작성자(작성자만 수정가능)
}
