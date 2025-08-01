package com.example.notfound_backend.service.admin;

import com.example.notfound_backend.data.dao.admin.ReportDAO;
import com.example.notfound_backend.data.dao.login.UserAuthDAO;
import com.example.notfound_backend.data.dao.admin.UserInfoDAO;
import com.example.notfound_backend.data.dto.admin.ReportAddDTO;
import com.example.notfound_backend.data.dto.admin.ReportResponseDTO;
import com.example.notfound_backend.data.dto.admin.ReportUpdateByAdnimDTO;
import com.example.notfound_backend.data.dto.admin.ReportUpdateDTO;
import com.example.notfound_backend.data.entity.admin.ReportEntity;
import com.example.notfound_backend.data.entity.enumlist.ReportStatus;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.exception.UnauthorizedAccessException;
import com.example.notfound_backend.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportDAO reportDAO;
    private final UserAuthDAO userAuthDAO;
    private final UserInfoDAO userInfoDAO;

    // 나의 신고 리스트
    public List<ReportResponseDTO> findByReporter(String reporter) {
        List<ReportEntity> reportEntityList = reportDAO.findByRporter(reporter);

        List<ReportResponseDTO> reportResponseDTOList = new ArrayList<>();
        for (ReportEntity reportEntity : reportEntityList) {
            ReportResponseDTO responseDTO = ReportResponseDTO.builder()
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
            reportResponseDTOList.add(responseDTO);
        }
        return reportResponseDTOList;
    }

    // 신고
    public ReportResponseDTO addReport(ReportAddDTO reportAddDTO) {
        UserAuthEntity reporter = userAuthDAO.findByUsername(reportAddDTO.getReporter());
        UserAuthEntity reported = userAuthDAO.findByUsername(reportAddDTO.getReported());
        if (reporter == null) {
            throw new UserNotFoundException("신고자 정보가 존재하지 않습니다");
        } else if (reported == null) {
            throw new UserNotFoundException("피신고자 정보가 존재하지 않습니다.");
        } else if (reporter.getUsername().equals(reported.getUsername())) {
            throw new IllegalArgumentException("자기자신은 신고할 수 없습니다.");
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

    // 신고 수정 (신고자)
    @Transactional
    public ReportResponseDTO updateReport(ReportUpdateDTO updateDTO) {
        ReportEntity existingReport = reportDAO.findReportById(updateDTO.getReportId());

        if (!existingReport.getReporter().getUsername().equals(updateDTO.getReporter())) {
            throw new UserNotFoundException("신고자 본인만 수정할 수 있습니다.");
        }
        existingReport.setReason(updateDTO.getReason());
        existingReport.setDescription(updateDTO.getDescription());
        existingReport.setUpdatedAt(LocalDateTime.now());

        ReportEntity updatedReport = reportDAO.updateReport(existingReport);

        return ReportResponseDTO.builder()
                .id(updatedReport.getId())
                .reason(updatedReport.getReason())
                .description(updatedReport.getDescription())
                .reporter(updatedReport.getReporter().getUsername())
                .reported(updatedReport.getReported().getUsername())
                .targetTable(updatedReport.getTargetTable())
                .targetId(updatedReport.getTargetId())
                .status(updatedReport.getStatus())
                .createdAt(updatedReport.getCreatedAt())
                .updatedAt(updatedReport.getUpdatedAt())
                .build();
    }

    // 신고 삭제 (신고자,관리자)
    @Transactional
    public void deleteReport(Integer reportId, String username)  {
        ReportEntity reportEntity = reportDAO.findReportById(reportId);
        if (!reportEntity.getReporter().getUsername().equals(username) && !userAuthDAO.getRole(username).equals("ROLE_ADMIN")) {
            throw new UnauthorizedAccessException("삭제 권한이 없습니다.");
        }
        reportDAO.deleteReportById(reportId);
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

    // 신고처리 (관리자)
    @Transactional
    public ReportResponseDTO updateReportByAdmin(ReportUpdateByAdnimDTO updateByAdnimDTO) {
        ReportEntity reportEntity = reportDAO.updateReportByAdmin(updateByAdnimDTO.getReportId(), updateByAdnimDTO.getStatus());
        if (reportEntity.getStatus() == ReportStatus.ACCEPTED) {
            String username = reportEntity.getReported().getUsername();
            userInfoDAO.updateWarning(username, 1);
        }
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
