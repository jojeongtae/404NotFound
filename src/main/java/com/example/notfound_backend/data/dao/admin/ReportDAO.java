package com.example.notfound_backend.data.dao.admin;

import com.example.notfound_backend.data.entity.admin.ReportEntity;
import com.example.notfound_backend.data.entity.enumlist.ReportStatus;
import com.example.notfound_backend.data.repository.admin.ReportRepository;
import com.example.notfound_backend.exception.ReportNotFoundException;
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

    // 신고 수정 (신고자)
    public ReportEntity updateReport(ReportEntity reportEntity) {
        return reportRepository.save(reportEntity); // 이미 영속 상태이므로 dirty checking 발생
    }

    // 신고 삭제 (신고자)
    public void deleteReportById(Integer reportId) {
        if (!reportRepository.existsById(reportId)) {
            throw new ReportNotFoundException("Report not found 삭제할 신고 존재하지 않음.");
        }
        reportRepository.deleteById(reportId);
    }

    // 신고리스트 (관리자)
    public List<ReportEntity> getAllReports() {
        return reportRepository.findAll();
    }

    // 신고처리 (관리자)
    public ReportEntity updateReportByAdmin(Integer reportId, ReportStatus newStatus) {
        ReportEntity report = reportRepository.findById(reportId).orElseThrow(() -> new UserNotFoundException("존재하지 않는 신고"));
        report.setStatus(newStatus);
        report.setUpdatedAt(LocalDateTime.now());
        return reportRepository.save(report);
    }

    public ReportEntity findReportById(Integer id) {
        return reportRepository.findById(id).orElseThrow(() -> new ReportNotFoundException("Report not found 삭제할 신고 존재하지 않음."));
    }



}
