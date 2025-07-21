package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.ReportAddDTO;
import com.example.notfound_backend.data.dto.ReportResponseDTO;
import com.example.notfound_backend.data.dto.ReportUpdateByAdnimDTO;
import com.example.notfound_backend.data.dto.ReportUpdateDTO;
import com.example.notfound_backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class ReportController {
    private final ReportService reportService;

    // 신고
    @PostMapping(value = "/user/report")
    public ResponseEntity<ReportResponseDTO> addReport(@RequestBody ReportAddDTO reportAddDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.addReport(reportAddDTO));
    }

    // 신고 수정 (신고자)
    @PutMapping(value = "/user/report/{reportId}")
    public ResponseEntity<ReportResponseDTO> updateReport(@PathVariable Integer reportId, @RequestBody ReportUpdateDTO reportUpdateDTO) {
        reportUpdateDTO.setReportId(reportId);
        ReportResponseDTO updatedReport = reportService.updateReport(reportUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedReport);
    }

    // 신고 삭제 (신고자,관리자)
    @DeleteMapping(value = "/user/report/{reportId}")
    public ResponseEntity<String> deleteReport(@PathVariable Integer reportId, @RequestParam String username) {
        reportService.deleteReport(reportId, username);
        return ResponseEntity.status(HttpStatus.OK).body("삭제성공");
    }

    // 신고리스트 (관리자)
    @GetMapping(value = "/admin/report-list")
    public ResponseEntity<List<ReportResponseDTO>> getAllReports() {
        return ResponseEntity.status(HttpStatus.OK).body(reportService.getAllReports());
    }

    // 신고처리 (관리자)
    @PatchMapping(value = "/admin/report")
    public ResponseEntity<ReportResponseDTO> updateReportByAdmin(@RequestBody ReportUpdateByAdnimDTO reportUpdateByAdnimDTO) {
        ReportResponseDTO response = reportService.updateReportByAdmin(reportUpdateByAdnimDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
