package com.example.notfound_backend.data.dto.admin;

import com.example.notfound_backend.data.entity.enumlist.ReportStatus;
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
