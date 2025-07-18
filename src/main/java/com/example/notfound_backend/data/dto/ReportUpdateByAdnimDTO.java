package com.example.notfound_backend.data.dto;

import com.example.notfound_backend.data.entity.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportUpdateByAdnimDTO {
    private Integer reportId;
    private ReportStatus status;
}
