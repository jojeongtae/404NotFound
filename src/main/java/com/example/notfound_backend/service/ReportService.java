package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.ReportDAO;
import com.example.notfound_backend.data.dao.UserAuthDAO;
import com.example.notfound_backend.data.dto.ReportAddDTO;
import com.example.notfound_backend.data.dto.ReportResponseDTO;
import com.example.notfound_backend.data.dto.ReportUpdateByAdnimDTO;
import com.example.notfound_backend.data.entity.ReportEntity;
import com.example.notfound_backend.data.entity.UserAuthEntity;
import com.example.notfound_backend.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportDAO reportDAO;
    private final UserAuthDAO userAuthDAO;

    // 신고
    public ReportResponseDTO addReport(ReportAddDTO reportAddDTO) {
        UserAuthEntity reporter = userAuthDAO.findByUsername(reportAddDTO.getReporter());
        UserAuthEntity reported = userAuthDAO.findByUsername(reportAddDTO.getReported());
        if (reporter == null || reported == null) {
            throw new UserNotFoundException("신고자 정보가 존재하지 않습니다");
        }

        ReportEntity reportEntity = ReportEntity.builder()
                .reason(reportAddDTO.getReason())
                .description(reportAddDTO.getDescription())
                .reporter(reporter)
                .reported(reported)
                .targetTable(reportAddDTO.getTargetTable())
                .targetId(reportAddDTO.getTargetId())
                .build();

        ReportEntity addedReport = reportDAO.addReport(reportEntity);
        return ReportResponseDTO.builder()
                .id(addedReport.getId())
                .reason(addedReport.getReason())
                .description(addedReport.getDescription())
                .reporter(addedReport.getReporter().getUsername())
                .reported(addedReport.getReported().getUsername())
                .targetTable(addedReport.getTargetTable())
                .targetId(addedReport.getTargetId())
                .status(addedReport.getStatus())
                .createdAt(addedReport.getCreatedAt())
                .updatedAt(addedReport.getUpdatedAt())
                .build();
    }

    // 신고리스트 (관리자)
    public List<ReportResponseDTO> getAllReports() {
        List<ReportEntity> reportEntities = reportDAO.getAllReports();
        List<ReportResponseDTO> reportResponseDTOS = new ArrayList<>();
        for (ReportEntity reportEntity : reportEntities) {
            ReportResponseDTO reportResponseDTO = ReportResponseDTO.builder()
                    .id(reportEntity.getId())
                    .reason(reportEntity.getReason())
                    .description(reportEntity.getDescription())
                    .reporter(reportEntity.getReporter().getUsername())
                    .reported(reportEntity.getReported().getUsername())
                    .targetTable(reportEntity.getTargetTable())
                    .targetId(reportEntity.getTargetId())
                    .status(reportEntity.getStatus())
                    .createdAt(reportEntity.getCreatedAt())
                    .updatedAt(reportEntity.getUpdatedAt())
                    .build();
            reportResponseDTOS.add(reportResponseDTO);
        }
        return reportResponseDTOS;
    }

    // 신고응답 (관리자)
    @Transactional
    public ReportResponseDTO updateReportByAdmin(ReportUpdateByAdnimDTO updateByAdnimDTO) {
        ReportEntity reportEntity = reportDAO.updateReportByAdmin(updateByAdnimDTO.getReportId(), updateByAdnimDTO.getStatus());
        return ReportResponseDTO.builder()
                .id(reportEntity.getId())
                .reason(reportEntity.getReason())
                .description(reportEntity.getDescription())
                .reporter(reportEntity.getReporter().getUsername())
                .reported(reportEntity.getReported().getUsername())
                .targetTable(reportEntity.getTargetTable())
                .targetId(reportEntity.getTargetId())
                .status(reportEntity.getStatus())
                .createdAt(reportEntity.getCreatedAt())
                .updatedAt(reportEntity.getUpdatedAt())
                .build();
    }

}
