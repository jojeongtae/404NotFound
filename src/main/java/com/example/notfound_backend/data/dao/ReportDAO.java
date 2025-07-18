package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.dto.ReportUpdateByAdnimDTO;
import com.example.notfound_backend.data.entity.ReportEntity;
import com.example.notfound_backend.data.entity.ReportStatus;
import com.example.notfound_backend.data.repository.ReportRepository;
import com.example.notfound_backend.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportDAO {
    private final ReportRepository reportRepository;

    // 신고
    public ReportEntity addReport(ReportEntity reportEntity) {
        ReportEntity savedReportEntity = ReportEntity.builder()
                .reason(reportEntity.getReason())
                .description(reportEntity.getDescription())
                .reporter(reportEntity.getReporter())
                .reported(reportEntity.getReported())
                .targetTable(reportEntity.getTargetTable())
                .targetId(reportEntity.getTargetId())
                .status(ReportStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
        return reportRepository.save(savedReportEntity);
    }

    // 신고리스트 (관리자)
    public List<ReportEntity> getAllReports() {
        return reportRepository.findAll();
    }

    // 신고응답 (관리자)
    public ReportEntity updateReportByAdmin(Integer reportId, ReportStatus newStatus) {
        ReportEntity report = reportRepository.findById(reportId).orElseThrow(() -> new UserNotFoundException("존재하지 않는 신고"));
        report.setStatus(newStatus);
        report.setUpdatedAt(LocalDateTime.now());
        return reportRepository.save(report);
    }

    public ReportEntity findReportById(Integer id) {
        return reportRepository.findById(id).orElse(null);
    }

}
